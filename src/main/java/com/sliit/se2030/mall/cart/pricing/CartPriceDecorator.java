package com.sliit.se2030.mall.cart.pricing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Decorator pattern -- the abstract Decorator. Holds the CartPrice it wraps
 * and by default passes everything through. A concrete decorator only says
 * how much it adds (or takes off) and what its line is called.
 */
public abstract class CartPriceDecorator implements CartPrice {

    protected final CartPrice wrapped;

    protected CartPriceDecorator(CartPrice wrapped) {
        this.wrapped = wrapped;
    }

    // Positive adds to the price, negative takes off, zero changes nothing.
    protected abstract BigDecimal adjustment();

    // null means "don't show a line for this" (e.g. no discount applies).
    protected abstract String label();

    @Override
    public BigDecimal getAmount() {
        return wrapped.getAmount().add(adjustment());
    }

    @Override
    public List<PriceLine> getBreakdown() {
        List<PriceLine> lines = new ArrayList<>(wrapped.getBreakdown());
        String label = label();
        if (label != null) {
            lines.add(new PriceLine(label, adjustment()));
        }
        return lines;
    }
}
