package com.sliit.se2030.mall.cart.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Decorator pattern -- a Concrete Decorator. Takes a percentage off when the
 * price it wraps reaches a threshold, e.g. 5% off orders of Rs. 50,000 or more.
 */
public class BulkDiscountDecorator extends CartPriceDecorator {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final BigDecimal threshold;
    private final BigDecimal percent;

    public BulkDiscountDecorator(CartPrice wrapped, BigDecimal threshold, BigDecimal percent) {
        super(wrapped);
        this.threshold = threshold;
        this.percent = percent;
    }

    private boolean applies() {
        return percent.signum() > 0 && wrapped.getAmount().compareTo(threshold) >= 0;
    }

    @Override
    protected BigDecimal adjustment() {
        if (!applies()) {
            return BigDecimal.ZERO;
        }
        return wrapped.getAmount().multiply(percent).divide(HUNDRED, 2, RoundingMode.HALF_UP).negate();
    }

    @Override
    protected String label() {
        return applies() ? "Discount (" + percent.stripTrailingZeros().toPlainString() + "% off)" : null;
    }
}
