package com.sliit.se2030.mall.order.service;

import com.sliit.se2030.mall.order.entity.PaymentStatus;

/** Provider-neutral outcome returned by a payment gateway. */
public record PaymentResult(PaymentStatus status, String paymentMethod, String transactionRef) {
}
