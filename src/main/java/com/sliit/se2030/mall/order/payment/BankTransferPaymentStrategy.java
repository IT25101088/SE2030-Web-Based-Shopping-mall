package com.sliit.se2030.mall.order.payment;

import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.order.entity.PaymentStatus;

/**
 * Strategy pattern -- a Concrete Strategy for online bank transfers.
 * Simulated: the transfer is treated as confirmed straight away.
 */
public class BankTransferPaymentStrategy implements PaymentStrategy {

    private final boolean simulateSuccess;

    public BankTransferPaymentStrategy(boolean simulateSuccess) {
        this.simulateSuccess = simulateSuccess;
    }

    @Override
    public PaymentResult pay(Order order) {
        String reference = TransactionReferenceGenerator.getInstance().next("BANK");
        PaymentStatus status = simulateSuccess ? PaymentStatus.SIMULATED_SUCCESS : PaymentStatus.SIMULATED_FAILED;
        return new PaymentResult(status, PaymentMethod.BANK_TRANSFER.name(), reference);
    }
}
