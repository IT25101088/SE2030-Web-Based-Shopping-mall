package com.sliit.se2030.mall.user.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    private String shippingAddress;

    protected Customer() {
        super();
    }

    public Customer(String email, String passwordHash, String fullName) {
        super(email, passwordHash, fullName, Role.CUSTOMER);
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
