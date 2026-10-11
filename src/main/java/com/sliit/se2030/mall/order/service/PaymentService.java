package com.sliit.se2030.mall.order.service;

import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.order.entity.Payment;
import com.sliit.se2030.mall.order.payment.PaymentMethod;
import com.sliit.se2030.mall.order.payment.PaymentResult;
import com.sliit.se2030.mall.order.payment.PaymentStrategy;
import com.sliit.se2030.mall.order.payment.PaymentStrategyFactory;
import com.sliit.se2030.mall.order.repository.PaymentRepository;
import org.springframework.stereotype.Service;

/**
 * Strategy pattern -- the Context. It doesn't know how any payment method
 * works: it asks the factory for the strategy matching the customer's choice,
 * calls pay(), and stores the result.
 */
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;

    public PaymentService(PaymentRepository paymentRepository, PaymentStrategyFactory paymentStrategyFactory) {
        this.paymentRepository = paymentRepository;
        this.paymentStrategyFactory = paymentStrategyFactory;
    }

    public Payment processPayment(Order order, PaymentMethod method) {
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(method);
        PaymentResult result = strategy.pay(order);
        Payment payment = new Payment(order, result.status(), result.paymentMethod(), result.transactionRef());
        return paymentRepository.save(payment);
    }
}
