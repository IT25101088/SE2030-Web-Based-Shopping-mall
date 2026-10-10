package com.sliit.se2030.mall.support.entity;

import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.common.entity.BaseEntity;
import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.user.entity.Customer;
import com.sliit.se2030.mall.user.entity.PlatformEmployee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * relatedOrder and relatedProduct are both nullable -- an inquiry must
 * reference at least one of them, but that's a business rule checked in
 * InquiryService, not a database constraint (keeping it simple, per the
 * project plan).
 */
@Entity
@Table(name = "inquiries")
public class Inquiry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_order_id")
    private Order relatedOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_product_id")
    private Product relatedProduct;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryStatus status = InquiryStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id")
    private PlatformEmployee assignedEmployee;

    protected Inquiry() {
    }

    public Inquiry(Customer customer, String subject, String message) {
        this.customer = customer;
        this.subject = subject;
        this.message = message;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Order getRelatedOrder() {
        return relatedOrder;
    }

    public void setRelatedOrder(Order relatedOrder) {
        this.relatedOrder = relatedOrder;
    }

    public Product getRelatedProduct() {
        return relatedProduct;
    }

    public void setRelatedProduct(Product relatedProduct) {
        this.relatedProduct = relatedProduct;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public InquiryStatus getStatus() {
        return status;
    }

    public void setStatus(InquiryStatus status) {
        this.status = status;
    }

    public PlatformEmployee getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(PlatformEmployee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }
}
