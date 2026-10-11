package com.sliit.se2030.mall.cart.pricing;

import java.math.BigDecimal;

/** One row of the price breakdown, e.g. "Delivery" / 350.00. Discounts are negative. */
public record PriceLine(String label, BigDecimal amount) {

    // JSP EL only finds bean-style getters, not record accessors like label().
    public String getLabel() {
        return label;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
