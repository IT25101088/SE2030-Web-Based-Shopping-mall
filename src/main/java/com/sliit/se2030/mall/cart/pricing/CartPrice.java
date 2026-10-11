package com.sliit.se2030.mall.cart.pricing;

import java.math.BigDecimal;
import java.util.List;

/**
 * Decorator pattern -- the Component interface. Both the plain item subtotal
 * and every decorator wrapped around it look the same to the cart page:
 * a final amount plus the lines that explain how it was reached.
 */
public interface CartPrice {

    BigDecimal getAmount();

    List<PriceLine> getBreakdown();
}
