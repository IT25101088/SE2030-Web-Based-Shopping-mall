package com.sliit.se2030.mall.order.payment;

import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.order.entity.PaymentStatus;

/**
 * Strategy pattern -- a Concrete Strategy for cash on delivery. Nothing can be
 * declined at checkout (the customer pays the courier later), so it always
 * succeeds, even when the other methods are set to simulate a decline.
 */
public class CashOnDeliveryPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult pay(Order order) {
        String reference = TransactionReferenceGenerator.getInstance().next("COD");
        return new PaymentResult(PaymentStatus.SIMULATED_SUCCESS, PaymentMethod.CASH_ON_DELIVERY.name(), reference);
    }
}
