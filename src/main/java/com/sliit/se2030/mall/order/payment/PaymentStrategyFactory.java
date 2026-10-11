package com.sliit.se2030.mall.order.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Factory pattern -- the Factory class. The only place that knows which
 * PaymentStrategy class goes with which PaymentMethod. Callers ask for a
 * method and get back "some PaymentStrategy", never a concrete class, so
 * adding a new method (e.g. a wallet) means one new strategy class plus one
 * new case here.
 */
@Component
public class PaymentStrategyFactory {

    private final boolean simulateSuccess;

    // Set mall.payment.dummy.always-succeed=false to try the declined-payment flow.
    public PaymentStrategyFactory(@Value("${mall.payment.dummy.always-succeed:true}") boolean simulateSuccess) {
        this.simulateSuccess = simulateSuccess;
    }

    public PaymentStrategy getStrategy(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("A payment method is required.");
        }
        return switch (method) {
            case CARD -> new CardPaymentStrategy(simulateSuccess);
            case BANK_TRANSFER -> new BankTransferPaymentStrategy(simulateSuccess);
            case CASH_ON_DELIVERY -> new CashOnDeliveryPaymentStrategy();
        };
    }
}
