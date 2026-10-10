package com.sliit.se2030.mall.order.service;

import com.sliit.se2030.mall.order.entity.Order;
import com.sliit.se2030.mall.order.entity.PaymentStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/** Internal payment provider for development; it never contacts an external service. */
@Component
public class DummyPaymentGateway implements PaymentGateway {

    private final boolean alwaysSucceed;

    public DummyPaymentGateway(@Value("${mall.payment.dummy.always-succeed:true}") boolean alwaysSucceed) {
        this.alwaysSucceed = alwaysSucceed;
    }

    @Override
    public PaymentResult process(Order order) {
        String reference = "DUMMY-" + UUID.randomUUID();
        if (alwaysSucceed) {
            return new PaymentResult(PaymentStatus.SIMULATED_SUCCESS, "INTERNAL_DUMMY", reference);
        }
        return new PaymentResult(PaymentStatus.SIMULATED_FAILED, "INTERNAL_DUMMY", reference);
    }
}
