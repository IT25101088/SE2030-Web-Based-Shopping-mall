package com.sliit.se2030.mall.support.service;

import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.catalog.repository.ProductRepository;
import com.sliit.se2030.mall.common.exception.AccessDeniedForResourceException;
import com.sliit.se2030.mall.common.exception.BusinessRuleViolationException;
import com.sliit.se2030.mall.common.exception.ResourceNotFoundException;
import com.sliit.se2030.mall.common.util.CurrentUserProvider;
import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.order.repository.OrderRepository;
import com.sliit.se2030.mall.support.dto.InquiryForm;
import com.sliit.se2030.mall.support.dto.InquiryResponseForm;
import com.sliit.se2030.mall.support.entity.Inquiry;
import com.sliit.se2030.mall.support.entity.InquiryResponse;
import com.sliit.se2030.mall.support.entity.InquiryStatus;
import com.sliit.se2030.mall.support.repository.InquiryRepository;
import com.sliit.se2030.mall.support.repository.InquiryResponseRepository;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.entity.PlatformEmployee;
import com.sliit.se2030.mall.user.entity.User;
import com.sliit.se2030.mall.user.repository.CustomerRepository;
import com.sliit.se2030.mall.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Customer Support and Inquiry System module. submitInquiry() requires at
 * least one of relatedOrderId/relatedProductId (checked here, not by a DB
 * constraint, per the entity's design comment).
 */
@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryResponseRepository inquiryResponseRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;

    public InquiryService(InquiryRepository inquiryRepository, InquiryResponseRepository inquiryResponseRepository,
                           OrderRepository orderRepository, ProductRepository productRepository,
                           CustomerRepository customerRepository, UserRepository userRepository,
                           CurrentUserProvider currentUserProvider) {
        this.inquiryRepository = inquiryRepository;
        this.inquiryResponseRepository = inquiryResponseRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
    }

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

        return inquiryRepository.save(inquiry);
    }

    public List<Inquiry> getInquiriesForCurrentCustomer() {
        Long customerId = currentUserProvider.getCurrentUserId();
        return inquiryRepository.findByCustomer_Id(customerId);
    }

    public List<Inquiry> listOpenInquiries() {
        return inquiryRepository.findByStatusNot(InquiryStatus.RESOLVED);
    }

    @Transactional
    public void respondToInquiry(Long inquiryId, InquiryResponseForm form) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found: " + inquiryId));

        Long responderId = currentUserProvider.getCurrentUserId();
        User respondedBy = userRepository.findById(responderId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + responderId));

        inquiryResponseRepository.save(new InquiryResponse(inquiry, form.getResponseText(), respondedBy));

        if (inquiry.getStatus() == InquiryStatus.OPEN) {
            inquiry.setStatus(InquiryStatus.IN_PROGRESS);
        }
        if (inquiry.getAssignedEmployee() == null && respondedBy instanceof PlatformEmployee employee) {
            inquiry.setAssignedEmployee(employee);
        }
    }

    @Transactional
    public void updateStatus(Long inquiryId, InquiryStatus status) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found: " + inquiryId));
        inquiry.setStatus(status);
    }
}
