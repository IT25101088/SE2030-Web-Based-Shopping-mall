package com.sliit.se2030.mall.order.entity;

import com.sliit.se2030.mall.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// Mocked/simulated payment only -- see PaymentStatus. No real gateway integration.
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false, unique = true)
    private String transactionRef;

    protected Payment() {
    }

    public Payment(Order order, PaymentStatus status, String paymentMethod, String transactionRef) {
        this.order = order;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.transactionRef = transactionRef;
    }

    public Order getOrder() {
        return order;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getTransactionRef() {
        return transactionRef;
    }
}
