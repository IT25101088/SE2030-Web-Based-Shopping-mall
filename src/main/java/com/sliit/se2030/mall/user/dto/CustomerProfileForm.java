package com.sliit.se2030.mall.user.dto;

import jakarta.validation.constraints.NotBlank;

// Deliberately has NO id or email field -- which customer this applies to is
// never taken from client input, and email is the login username so it stays
// fixed here. See CustomerProfileService.
public class CustomerProfileForm {

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String phone;

    private String shippingAddress;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
