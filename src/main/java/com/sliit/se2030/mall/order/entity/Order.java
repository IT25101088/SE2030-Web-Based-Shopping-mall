package com.sliit.se2030.mall.order.entity;

import com.sliit.se2030.mall.common.entity.BaseEntity;
import com.sliit.se2030.mall.user.entity.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

// Named "orders", not "order" -- ORDER is a reserved SQL keyword (as in ORDER BY).
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // A rollup of this order's OrderItem statuses (an order can span multiple
    // merchants' items) -- always set via OrderService.recomputeOrderStatus(),
    // never directly by a merchant. See OrderItem.status for the per-item value
    // merchants actually control.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    // Snapshot at checkout time -- never recomputed later from live product prices.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // Snapshot copy, not a live FK to the customer's address -- a later address
    // edit must not silently rewrite what was actually shipped for this order.
    @Column(nullable = false)
    private String shippingAddress;

    protected Order() {
    }

    public Order(Customer customer, BigDecimal totalAmount, String shippingAddress) {
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
    }

    public Customer getCustomer() {
        return customer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }
}
