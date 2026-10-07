package com.sliit.se2030.mall.order.service;

import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.order.entity.Payment;
import com.sliit.se2030.mall.order.entity.PaymentStatus;
import com.sliit.se2030.mall.order.repository.PaymentRepository;
import org.springframework.stereotype.Service;

// Persists provider-neutral gateway results; provider-specific logic belongs in PaymentGateway.
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentRepository paymentRepository, PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
    }

    public Payment processPayment(Order order) {
        PaymentResult result = paymentGateway.process(order);
        Payment payment = new Payment(order, result.status(), result.paymentMethod(), result.transactionRef());
        return paymentRepository.save(payment);
    }
}
