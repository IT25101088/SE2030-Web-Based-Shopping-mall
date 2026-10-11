package com.sliit.se2030.mall.order.payment;

import com.sliit.se2030.mall.order.entity.PaymentStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Plain unit test of the payment Strategy + Factory + Singleton classes. */
class PaymentStrategyFactoryTest {

    @Test
    void factory_returnsTheStrategyForEachMethod() {
        PaymentStrategyFactory factory = new PaymentStrategyFactory(true);

        assertThat(factory.getStrategy(PaymentMethod.CARD)).isInstanceOf(CardPaymentStrategy.class);
        assertThat(factory.getStrategy(PaymentMethod.BANK_TRANSFER)).isInstanceOf(BankTransferPaymentStrategy.class);
        assertThat(factory.getStrategy(PaymentMethod.CASH_ON_DELIVERY)).isInstanceOf(CashOnDeliveryPaymentStrategy.class);
    }

    @Test
    void strategies_recordTheirOwnMethod() {
        PaymentStrategyFactory factory = new PaymentStrategyFactory(true);

        PaymentResult result = factory.getStrategy(PaymentMethod.BANK_TRANSFER).pay(null);

        assertThat(result.status()).isEqualTo(PaymentStatus.SIMULATED_SUCCESS);
        assertThat(result.paymentMethod()).isEqualTo("BANK_TRANSFER");
        assertThat(result.transactionRef()).startsWith("BANK-");
    }

    @Test
    void whenDeclinesAreSimulated_cardFailsButCashOnDeliveryStillSucceeds() {
        PaymentStrategyFactory factory = new PaymentStrategyFactory(false);

        assertThat(factory.getStrategy(PaymentMethod.CARD).pay(null).status())
                .isEqualTo(PaymentStatus.SIMULATED_FAILED);
        assertThat(factory.getStrategy(PaymentMethod.CASH_ON_DELIVERY).pay(null).status())
                .isEqualTo(PaymentStatus.SIMULATED_SUCCESS);
    }

    @Test
    void referenceGenerator_isOneSharedInstance_andNeverRepeatsAReference() {
        TransactionReferenceGenerator first = TransactionReferenceGenerator.getInstance();
        TransactionReferenceGenerator second = TransactionReferenceGenerator.getInstance();

        assertThat(first).isSameAs(second);
        assertThat(first.next("CARD")).isNotEqualTo(second.next("CARD"));
    }
}
