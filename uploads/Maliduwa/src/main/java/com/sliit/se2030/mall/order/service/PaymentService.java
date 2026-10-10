package com.sliit.se2030.mall.order.service;

import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.order.entity.Payment;
import com.sliit.se2030.mall.order.entity.PaymentStatus;
import com.sliit.se2030.mall.order.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

// Payment is simulated/mocked per the project scope, no real gateway -- it always succeeds.
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment simulatePayment(Order order) {
        Payment payment = new Payment(order, PaymentStatus.SIMULATED_SUCCESS, "SIMULATED",
                UUID.randomUUID().toString());
        return paymentRepository.save(payment);
    }
}
