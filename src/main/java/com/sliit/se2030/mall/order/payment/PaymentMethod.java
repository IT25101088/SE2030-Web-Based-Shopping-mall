package com.sliit.se2030.mall.order.payment;

/** The ways a customer can pay at checkout. Each one has its own PaymentStrategy. */
public enum PaymentMethod {
    CARD("Credit / debit card"),
    BANK_TRANSFER("Bank transfer"),
    CASH_ON_DELIVERY("Cash on delivery");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    // Shown next to the radio button on the checkout page.
    public String getLabel() {
        return label;
    }
}
