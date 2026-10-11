package com.sliit.se2030.mall.cart.pricing;

import com.sliit.se2030.mall.cart.entity.CartItem;
import com.sliit.se2030.mall.catalog.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Plain unit test of the cart price decorators: delivery Rs. 350, free from 10,000, 5% off from 50,000. */
class CartPriceCalculatorTest {

    private final CartPriceCalculator calculator = new CartPriceCalculator(
            new BigDecimal("350"), new BigDecimal("10000"), new BigDecimal("50000"), new BigDecimal("5"));

    private static List<CartItem> itemsWorth(String price, int quantity) {
        Product product = new Product("Mug", new BigDecimal(price), 10, null);
        return List.of(new CartItem(null, product, quantity));
    }

    @Test
    void smallOrder_paysDelivery() {
        CartPrice price = calculator.priceOf(itemsWorth("1500.00", 2));

        assertThat(price.getAmount()).isEqualByComparingTo("3350.00");
        assertThat(price.getBreakdown()).extracting(PriceLine::label).containsExactly("Subtotal", "Delivery");
    }

    @Test
    void orderOverFreeDeliveryThreshold_hasFreeDelivery() {
        CartPrice price = calculator.priceOf(itemsWorth("12000.00", 1));

        assertThat(price.getAmount()).isEqualByComparingTo("12000.00");
        assertThat(price.getBreakdown()).extracting(PriceLine::label).containsExactly("Subtotal", "Delivery (free)");
    }

    @Test
    void largeOrder_getsDiscountAndFreeDelivery() {
        CartPrice price = calculator.priceOf(itemsWorth("30000.00", 2));

        // 60,000 - 5% (3,000) = 57,000, delivery free.
        assertThat(price.getAmount()).isEqualByComparingTo("57000.00");
        assertThat(price.getBreakdown()).extracting(PriceLine::label)
                .containsExactly("Subtotal", "Discount (5% off)", "Delivery (free)");
    }

    @Test
    void emptyCart_costsNothing() {
        CartPrice price = calculator.priceOf(List.of());

        assertThat(price.getAmount()).isEqualByComparingTo("0");
        assertThat(price.getBreakdown()).extracting(PriceLine::label).containsExactly("Subtotal");
    }
}
