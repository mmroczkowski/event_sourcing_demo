package org.demo.test.util;

import org.demo.write.aggregate.account.event.AccountOpenedEvent;
import org.demo.write.aggregate.account.event.PaymentExecutedEvent;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Utility class for unit test data.
 *
 * @author mmroczkowski
 */
public class TestData {

    public static final UUID TEST_UUID = UUID.randomUUID();
    public static final AccountOpenedEvent TEST_ACCOUNT_OPENED_EVENT =
            new AccountOpenedEvent(TEST_UUID, "12345", BigDecimal.valueOf(1000), BigDecimal.valueOf(500));
    public static final PaymentExecutedEvent TEST_PAYMENT_EXECUTED_EVENT = new PaymentExecutedEvent(TEST_UUID, BigDecimal.valueOf(500));
}
