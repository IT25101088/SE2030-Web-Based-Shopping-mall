package com.sliit.se2030.mall.order.dto;

import jakarta.validation.constraints.NotBlank;

public class CheckoutForm {

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
