package com.sliit.se2030.mall.order.entity;

import com.sliit.se2030.mall.catalog.entity.Product;
import com.sliit.se2030.mall.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Denormalized copy of product.merchant.id at order time -- lets a merchant
    // query "my order items" directly without joining through Product, and it
    // survives even if the product is later deleted or reassigned.
    @Column(nullable = false)
    private Long merchantId;

    @Column(nullable = false)
    private int quantitySnapshot;

    // Never read the live product price for a historical order -- prices change.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceSnapshot;

    // Per-item fulfillment status. An Order can span multiple merchants (this is
    // a multi-merchant mall), so each merchant advances only their own items --
    // Order.status is a computed rollup of these, never set directly by a merchant.
    // See OrderService.recomputeOrderStatus().
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    protected OrderItem() {
    }

    public OrderItem(Order order, Product product, Long merchantId, int quantitySnapshot, BigDecimal unitPriceSnapshot) {
        this.order = order;
        this.product = product;
        this.merchantId = merchantId;
        this.quantitySnapshot = quantitySnapshot;
        this.unitPriceSnapshot = unitPriceSnapshot;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public int getQuantitySnapshot() {
        return quantitySnapshot;
    }

    public BigDecimal getUnitPriceSnapshot() {
        return unitPriceSnapshot;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
