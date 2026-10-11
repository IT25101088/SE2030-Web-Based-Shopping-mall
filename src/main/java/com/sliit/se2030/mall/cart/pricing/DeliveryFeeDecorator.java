package com.sliit.se2030.mall.cart.pricing;

import java.math.BigDecimal;

/**
 * Decorator pattern -- a Concrete Decorator. Adds a flat delivery fee, which
 * is waived once the price it wraps reaches the free-delivery threshold.
 * An empty cart gets no delivery line at all.
 */
public class DeliveryFeeDecorator extends CartPriceDecorator {

    private final BigDecimal fee;
    private final BigDecimal freeFrom;

    public DeliveryFeeDecorator(CartPrice wrapped, BigDecimal fee, BigDecimal freeFrom) {
        super(wrapped);
        this.fee = fee;
        this.freeFrom = freeFrom;
    }

    private boolean isEmpty() {
        return wrapped.getAmount().signum() <= 0;
    }

    private boolean isFree() {
        return wrapped.getAmount().compareTo(freeFrom) >= 0;
    }

    @Override
    protected BigDecimal adjustment() {
        return isEmpty() || isFree() ? BigDecimal.ZERO : fee;
    }

    @Override
    protected String label() {
        if (isEmpty()) {
            return null;
        }
        return isFree() ? "Delivery (free)" : "Delivery";
    }
}
