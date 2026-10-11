package com.sliit.se2030.mall.cart.pricing;

import com.sliit.se2030.mall.cart.entity.CartItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Decorator pattern -- the client that puts the wrappers together. The order
 * matters: the discount is worked out on the item subtotal, and free delivery
 * is checked against the price after the discount. Adding a new charge
 * (e.g. gift wrapping) means one new decorator class and one more line here.
 *
 * The amounts come from application.properties (mall.pricing.*).
 */
@Component
public class CartPriceCalculator {

    private final BigDecimal deliveryFee;
    private final BigDecimal freeDeliveryFrom;
    private final BigDecimal discountFrom;
    private final BigDecimal discountPercent;

    public CartPriceCalculator(@Value("${mall.pricing.delivery-fee:350}") BigDecimal deliveryFee,
                               @Value("${mall.pricing.free-delivery-from:10000}") BigDecimal freeDeliveryFrom,
                               @Value("${mall.pricing.discount-from:50000}") BigDecimal discountFrom,
                               @Value("${mall.pricing.discount-percent:5}") BigDecimal discountPercent) {
        this.deliveryFee = deliveryFee;
        this.freeDeliveryFrom = freeDeliveryFrom;
        this.discountFrom = discountFrom;
        this.discountPercent = discountPercent;
    }

    public CartPrice priceOf(List<CartItem> items) {
        CartPrice price = new CartSubtotal(items);
        price = new BulkDiscountDecorator(price, discountFrom, discountPercent);
        price = new DeliveryFeeDecorator(price, deliveryFee, freeDeliveryFrom);
        return price;
    }
}
