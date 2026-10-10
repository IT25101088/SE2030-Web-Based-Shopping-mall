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
        // TODO: implement submitInquiry -- see NOTES.md, "InquiryService.submitInquiry()".
        // Require at least one of relatedOrderId/relatedProductId (this is a
        // service-layer rule, not a DB constraint); if an order is given, verify it
        // belongs to the current customer before attaching it.
        throw new UnsupportedOperationException("TODO: implement submitInquiry()");
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
        // TODO: implement respondToInquiry -- see NOTES.md, "InquiryService.respondToInquiry()".
        // Save the response; if the inquiry was OPEN, advance it to IN_PROGRESS; if
        // no employee is assigned yet and the responder is a PlatformEmployee
        // (instanceof pattern match), assign them.
        throw new UnsupportedOperationException("TODO: implement respondToInquiry()");
    }

    @Transactional
    public void updateStatus(Long inquiryId, InquiryStatus status) {
        // TODO: implement updateStatus -- see NOTES.md, "InquiryService.updateStatus()".
        throw new UnsupportedOperationException("TODO: implement updateStatus()");
    }
}
