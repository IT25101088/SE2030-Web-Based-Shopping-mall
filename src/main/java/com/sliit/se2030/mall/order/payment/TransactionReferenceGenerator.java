package com.sliit.se2030.mall.order.payment;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Singleton pattern. Every payment needs a unique transaction reference (the
 * payments table enforces it), and all strategies share one running counter
 * through this single instance, so two payments in the same millisecond
 * still get different references.
 *
 * Written by hand on purpose: it isn't a Spring bean, because the strategies
 * are plain objects created by PaymentStrategyFactory with "new".
 */
public final class TransactionReferenceGenerator {

    private static TransactionReferenceGenerator instance;

    private final AtomicLong sequence = new AtomicLong();

    // Private, so no other class can create a second generator.
    private TransactionReferenceGenerator() {
    }

    // synchronized: two checkouts at the same moment must not both see
    // instance == null and create two generators.
    public static synchronized TransactionReferenceGenerator getInstance() {
        if (instance == null) {
            instance = new TransactionReferenceGenerator();
        }
        return instance;
    }

    // e.g. "CARD-1760160000000-42". The timestamp keeps references unique
    // across restarts, when the counter starts again from 1.
    public String next(String prefix) {
        return prefix + "-" + System.currentTimeMillis() + "-" + sequence.incrementAndGet();
    }
}
