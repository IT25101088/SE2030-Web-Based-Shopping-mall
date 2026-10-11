package com.sliit.se2030.mall.order.dto;

import com.sliit.se2030.mall.order.payment.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CheckoutForm {

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    // Card is pre-selected on the checkout page.
    @NotNull(message = "Choose a payment method")
    private PaymentMethod paymentMethod = PaymentMethod.CARD;

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
