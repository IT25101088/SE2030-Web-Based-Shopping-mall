package com.sliit.se2030.mall.order.payment;

import com.sliit.se2030.mall.order.entity.Order;

/**
 * Strategy pattern -- the Strategy interface. Every payment method pays for an
 * order in its own way, but PaymentService only ever calls pay(), so it never
 * needs an if/else on the method the customer picked.
 */
public interface PaymentStrategy {

    PaymentResult pay(Order order);
}
