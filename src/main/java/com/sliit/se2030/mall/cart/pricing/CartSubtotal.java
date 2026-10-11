package com.sliit.se2030.mall.cart.pricing;

import com.sliit.se2030.mall.cart.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Decorator pattern -- the Concrete Component. Just price x quantity for
 * every item; delivery and discounts are added by wrapping this in decorators.
 */
public class CartSubtotal implements CartPrice {

    private final BigDecimal amount;

    public CartSubtotal(List<CartItem> items) {
        this.amount = items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public List<PriceLine> getBreakdown() {
        return List.of(new PriceLine("Subtotal", amount));
    }
}
