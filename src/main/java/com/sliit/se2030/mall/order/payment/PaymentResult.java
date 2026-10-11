package com.sliit.se2030.mall.order.payment;

import com.sliit.se2030.mall.order.entity.PaymentStatus;

/** Method-neutral outcome returned by every PaymentStrategy. */
public record PaymentResult(PaymentStatus status, String paymentMethod, String transactionRef) {
}
