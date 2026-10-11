package com.sliit.se2030.mall.order.payment;

import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.order.entity.PaymentStatus;

/**
 * Strategy pattern -- a Concrete Strategy for card payments. Simulated: no
 * card details are collected and no card network is contacted.
 */
public class CardPaymentStrategy implements PaymentStrategy {

    private final boolean simulateSuccess;

    public CardPaymentStrategy(boolean simulateSuccess) {
        this.simulateSuccess = simulateSuccess;
    }

    @Override
    public PaymentResult pay(Order order) {
        String reference = TransactionReferenceGenerator.getInstance().next("CARD");
        PaymentStatus status = simulateSuccess ? PaymentStatus.SIMULATED_SUCCESS : PaymentStatus.SIMULATED_FAILED;
        return new PaymentResult(status, PaymentMethod.CARD.name(), reference);
    }
}
