package com.sliit.se2030.mall.support.service;

import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.catalog.repository.ProductRepository;
import com.sliit.se2030.mall.common.exception.AccessDeniedForResourceException;
import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.order.entity.OrderItem;
import com.sliit.se2030.mall.order.repository.OrderItemRepository;
import com.sliit.se2030.mall.order.repository.OrderRepository;
import com.sliit.se2030.mall.support.dto.InquiryForm;
import com.sliit.se2030.mall.support.dto.InquiryResponseForm;
import com.sliit.se2030.mall.support.entity.Inquiry;
import com.sliit.se2030.mall.support.entity.InquiryResponse;
import com.sliit.se2030.mall.support.entity.InquiryStatus;
import com.sliit.se2030.mall.support.repository.InquiryRepository;
import com.sliit.se2030.mall.support.repository.InquiryResponseRepository;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.entity.Merchant;
import com.sliit.se2030.mall.user.entity.PlatformEmployee;
import com.sliit.se2030.mall.user.entity.User;
import com.sliit.se2030.mall.user.entity.VerificationStatus;
import com.sliit.se2030.mall.user.repository.CustomerRepository;
import com.sliit.se2030.mall.user.repository.MerchantRepository;
import com.sliit.se2030.mall.user.repository.PlatformEmployeeRepository;
import com.sliit.se2030.mall.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Customer Support and Inquiry System module. submitInquiry() requires at
 * least one of relatedOrderId/relatedProductId (checked here, not by a DB
 * constraint, per the entity's design comment).
 *
 * The flow:
 * 1. A customer opens an inquiry (OPEN). It always lands with the mall team.
 * 2. A platform employee either answers it themselves, or forwards it to a
 *    shop (IN_PROGRESS, inquiry.merchant set).
 * 3. The shop answers with an internal note the customer never sees. That
 *    hands the inquiry straight back to the mall team (AWAITING_REVIEW,
 *    inquiry.merchant cleared).
 * 4. The employee reviews the note, then replies to the customer, forwards
 *    it again, or marks it RESOLVED. Only employees can resolve.
 */
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryResponseRepository inquiryResponseRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;
    private final PlatformEmployeeRepository platformEmployeeRepository;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;

    public InquiryService(InquiryRepository inquiryRepository, InquiryResponseRepository inquiryResponseRepository,
                           OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                           ProductRepository productRepository,
                           CustomerRepository customerRepository, MerchantRepository merchantRepository,
                           PlatformEmployeeRepository platformEmployeeRepository, UserRepository userRepository,
                           CurrentUserProvider currentUserProvider) {
        this.inquiryRepository = inquiryRepository;
        this.inquiryResponseRepository = inquiryResponseRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.platformEmployeeRepository = platformEmployeeRepository;
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
    }

    // ---- Customer ----

    @Transactional
    public Inquiry submitInquiry(InquiryForm form) {
        if (form.getRelatedOrderId() == null && form.getRelatedProductId() == null) {
            throw new BusinessRuleViolationException("An inquiry must reference an order or a product.");
        }

        Long customerId = currentUserProvider.getCurrentUserId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));

        Inquiry inquiry = new Inquiry(customer, form.getSubject(), form.getMessage());

        if (form.getRelatedOrderId() != null) {
            Order order = orderRepository.findById(form.getRelatedOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + form.getRelatedOrderId()));
            if (!order.getCustomer().getId().equals(customerId)) {
                throw new AccessDeniedForResourceException("This order does not belong to you.");
            }
            inquiry.setRelatedOrder(order);
        }

        if (form.getRelatedProductId() != null) {
            Product product = productRepository.findById(form.getRelatedProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found: " + form.getRelatedProductId()));
            inquiry.setRelatedProduct(product);
        }

        // No merchant is set here on purpose: every new inquiry lands with the
        // mall team first, who decide whether to forward it to a shop.
        return inquiryRepository.save(inquiry);
    }

    public List<Inquiry> getInquiriesForCurrentCustomer() {
        Long customerId = currentUserProvider.getCurrentUserId();
        return inquiryRepository.findByCustomer_Id(customerId);
    }

    // Fills the "Which order?" dropdown on the inquiry form: only the customer's
    // own orders, newest first. submitInquiry() still re-checks ownership, since
    // a dropdown can be tampered with like any other form field.
    public List<Order> getOrdersForCurrentCustomer() {
        Long customerId = currentUserProvider.getCurrentUserId();
        return orderRepository.findByCustomer_Id(customerId).stream()
                .sorted(Comparator.comparing(Order::getId).reversed())
                .toList();
    }

    // ---- Platform employee ----

    public List<Inquiry> listOpenInquiries() {
        return inquiryRepository.findByStatusNot(InquiryStatus.RESOLVED);
    }

    // The shops an employee can forward an inquiry to.
    public List<Merchant> listApprovedMerchants() {
        return merchantRepository.findByVerificationStatus(VerificationStatus.APPROVED);
    }

    /**
     * The shops each inquiry is most likely about, keyed by inquiry id, so the
     * employee can forward with one click instead of searching the dropdown:
     * - about a product: the shop that sells it;
     * - about an order: every shop with an item in that order (one order can
     *   contain items from several shops).
     * Only approved shops are suggested, since forwardToMerchant() rejects the rest.
     * This only suggests -- nothing is forwarded until the employee clicks.
     */
    public Map<Long, List<Merchant>> suggestShops(List<Inquiry> inquiries) {
        Map<Long, List<Merchant>> suggestions = new HashMap<>();
        for (Inquiry inquiry : inquiries) {
            Set<Long> merchantIds = new LinkedHashSet<>();
            if (inquiry.getRelatedProduct() != null) {
                merchantIds.add(inquiry.getRelatedProduct().getMerchant().getId());
            }
            if (inquiry.getRelatedOrder() != null) {
                for (OrderItem item : orderItemRepository.findByOrder_Id(inquiry.getRelatedOrder().getId())) {
                    merchantIds.add(item.getMerchantId());
                }
            }
            List<Merchant> shops = merchantRepository.findAllById(merchantIds).stream()
                    .filter(merchant -> merchant.getVerificationStatus() == VerificationStatus.APPROVED)
                    .toList();
            suggestions.put(inquiry.getId(), shops);
        }
        return suggestions;
    }

    // An employee's reply always goes to the customer (internalNote = false).
    @Transactional
    public void respondAsEmployee(Long inquiryId, InquiryResponseForm form) {
        Inquiry inquiry = findUnresolvedInquiry(inquiryId);
        PlatformEmployee employee = currentEmployee();

        inquiryResponseRepository.save(new InquiryResponse(inquiry, form.getResponseText(), employee, false));
        // Replying after a shop's hand-back counts as the employee having reviewed it.
        if (inquiry.getStatus() == InquiryStatus.OPEN || inquiry.getStatus() == InquiryStatus.AWAITING_REVIEW) {
            inquiry.setStatus(InquiryStatus.IN_PROGRESS);
        }
        claimForEmployee(inquiry, employee);
    }

    @Transactional
    public void forwardToMerchant(Long inquiryId, Long merchantId) {
        Inquiry inquiry = findUnresolvedInquiry(inquiryId);
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found: " + merchantId));
        if (merchant.getVerificationStatus() != VerificationStatus.APPROVED) {
            throw new BusinessRuleViolationException("Inquiries can only be forwarded to approved shops.");
        }

        inquiry.setMerchant(merchant);
        inquiry.setStatus(InquiryStatus.IN_PROGRESS);
        claimForEmployee(inquiry, currentEmployee());
    }

    @Transactional
    public void takeBackFromMerchant(Long inquiryId) {
        Inquiry inquiry = findUnresolvedInquiry(inquiryId);
        if (inquiry.getMerchant() == null) {
            throw new BusinessRuleViolationException("This request is not with a shop.");
        }

        inquiry.setMerchant(null);
        claimForEmployee(inquiry, currentEmployee());
    }

    // Only reachable from EmployeeInquiryController, so only the mall team can resolve.
    @Transactional
    public void resolve(Long inquiryId) {
        Inquiry inquiry = findUnresolvedInquiry(inquiryId);
        inquiry.setStatus(InquiryStatus.RESOLVED);
        claimForEmployee(inquiry, currentEmployee());
    }

    // ---- Merchant ----

    public List<Inquiry> getInquiriesForCurrentMerchant() {
        Long merchantId = currentUserProvider.getCurrentUserId();
        return inquiryRepository.findByMerchant_IdAndStatusNot(merchantId, InquiryStatus.RESOLVED);
    }

    // The shop's answer is an internal note for the mall team, and sending it
    // hands the inquiry back: it leaves the shop's list and waits for review.
    @Transactional
    public void respondAsMerchant(Long inquiryId, InquiryResponseForm form) {
        Inquiry inquiry = findInquiryForCurrentMerchant(inquiryId);
        User merchant = userRepository.findById(currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));

        inquiryResponseRepository.save(new InquiryResponse(inquiry, form.getResponseText(), merchant, true));
        inquiry.setMerchant(null);
        inquiry.setStatus(InquiryStatus.AWAITING_REVIEW);
    }

    // ---- Helpers ----

    private Inquiry findUnresolvedInquiry(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found: " + inquiryId));
        if (inquiry.getStatus() == InquiryStatus.RESOLVED) {
            throw new BusinessRuleViolationException("This request is already resolved.");
        }
        return inquiry;
    }

    // A merchant may only touch inquiries the mall team forwarded to their own shop.
    private Inquiry findInquiryForCurrentMerchant(Long inquiryId) {
        Inquiry inquiry = findUnresolvedInquiry(inquiryId);
        Long merchantId = currentUserProvider.getCurrentUserId();
        if (inquiry.getMerchant() == null || !inquiry.getMerchant().getId().equals(merchantId)) {
            throw new AccessDeniedForResourceException("This request has not been forwarded to your shop.");
        }
        return inquiry;
    }

    private PlatformEmployee currentEmployee() {
        Long employeeId = currentUserProvider.getCurrentUserId();
        return platformEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));
    }

    // The first employee to act on an inquiry becomes its owner on the mall side.
    private void claimForEmployee(Inquiry inquiry, PlatformEmployee employee) {
        if (inquiry.getAssignedEmployee() == null) {
            inquiry.setAssignedEmployee(employee);
        }
    }
}
