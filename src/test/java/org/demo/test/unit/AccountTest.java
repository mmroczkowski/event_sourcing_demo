package org.demo.test.unit;

import org.demo.write.aggregate.BusinessException;
import org.demo.write.aggregate.Event;
import org.demo.write.aggregate.account.Account;
import org.demo.write.aggregate.account.event.PaymentExecutedEvent;
import org.demo.write.aggregate.account.exception.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.demo.test.util.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link Account} class.
 *
 * @author mmroczkowski
 */
public class AccountTest {

    private Account account;

    @BeforeEach
    void init() {
        account = new Account(TEST_UUID);
    }

    /**
     * This test verifies if the handle method is able to choose specific handlers for all events.
     */
    @Test
    public void testGeneralHandle() throws BusinessException {
        // when
        account.handle((Event) TEST_ACCOUNT_OPENED_EVENT);
        account.handle((Event) TEST_PAYMENT_EXECUTED_EVENT);

        // then
        assertEquals(BigDecimal.valueOf(1500), account.getBalance());
    }

    /**
     * This test verifies if the account number, balance, and credit line of the account
     * are set correctly when an AccountOpenedEvent is handled.
     */
    @Test
    public void testHandleAccountOpenedEvent() {
        // when
        account.handle(TEST_ACCOUNT_OPENED_EVENT);

        // then
        assertEquals("12345", account.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), account.getBalance());
        assertEquals(BigDecimal.valueOf(500), account.getCreditLine());
    }

    /**
     * This test verifies if the balance of the account is updated correctly
     * when a PaymentExecutedEvent is handled.
     */
    @Test
    public void testHandlePaymentExecutedEvent() throws BusinessException {
        // when
        account.handle(TEST_ACCOUNT_OPENED_EVENT);
        account.handle(TEST_PAYMENT_EXECUTED_EVENT);

        // then
        assertEquals(BigDecimal.valueOf(1500), account.getBalance());
    }

    /**
     * This test verifies if the {@link InsufficientBalanceException} is thrown
     * when there is not enough balance to process the payment.
     */
    @Test
    public void testInsufficientBalance() {
        assertThrows(InsufficientBalanceException.class, () -> {
            // when
            account.handle(TEST_ACCOUNT_OPENED_EVENT);

            // then
            account.handle(new PaymentExecutedEvent(TEST_UUID, BigDecimal.valueOf(-2000)));
        });
    }
}
