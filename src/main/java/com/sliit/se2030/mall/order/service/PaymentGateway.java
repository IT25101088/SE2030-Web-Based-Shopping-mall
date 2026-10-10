package com.sliit.se2030.mall.order.service;

import com.sliit.se2030.mall.order.entity.Order;

/** Processes a payment without coupling order handling to a provider. */
public interface PaymentGateway {

    PaymentResult process(Order order);
}
