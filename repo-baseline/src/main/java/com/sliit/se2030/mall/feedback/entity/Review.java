package com.sliit.se2030.mall.feedback.entity;

import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.common.entity.BaseEntity;
import com.sliit.se2030.mall.order.entity.OrderItem;
import com.sliit.se2030.mall.user.entity.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * verifiedOrderItem is NOT NULL and unique per (customer, order item) -- this
 * IS the enforcement mechanism for "verified-purchase-only reviews". A review
 * can only ever be created by pointing at a specific OrderItem the reviewing
 * customer actually purchased; the service layer must additionally check that
 * orderItem.getOrder().getCustomer() equals the current customer and the
 * order is DELIVERED before allowing the save.
 */
@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "verified_order_item_id"}))
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "verified_order_item_id", nullable = false)
    private OrderItem verifiedOrderItem;

    protected Review() {
    }

    public Review(Customer customer, Product product, int rating, String comment, OrderItem verifiedOrderItem) {
        this.customer = customer;
        this.product = product;
        this.rating = rating;
        this.comment = comment;
        this.verifiedOrderItem = verifiedOrderItem;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public OrderItem getVerifiedOrderItem() {
        return verifiedOrderItem;
    }
}
