package org.demo.test.unit;

import org.demo.read.endpoint.account.model.TransactionHistoryEntry;
import org.demo.read.endpoint.account.model.TransactionType;
import org.demo.write.aggregate.Event;
import org.demo.write.aggregate.account.event.AccountOpenedEvent;
import org.demo.write.aggregate.account.event.PaymentExecutedEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.demo.test.util.TestData.TEST_ACCOUNT_OPENED_EVENT;
import static org.demo.test.util.TestData.TEST_UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link TransactionHistoryEntry} class.
 *
 * @author mmroczkowski
 */
public class TransactionHistoryEntryTest {

    private static final PaymentExecutedEvent TEST_PAYMENT_EXECUTED_EVENT_CREDIT = new PaymentExecutedEvent(TEST_UUID, BigDecimal.valueOf(500));
    private static final PaymentExecutedEvent TEST_PAYMENT_EXECUTED_EVENT_DEBIT = new PaymentExecutedEvent(TEST_UUID, BigDecimal.valueOf(-500));

    /**
     * Tests the general behavior of the {@link TransactionHistoryEntry#handle(Event)} method.
     * Verifies that the method returns TransactionHistoryEntry for both {@link AccountOpenedEvent} and {@link PaymentExecutedEvent}.
     */
    @Test
    public void testGeneralHandle() {
        // when
        Optional<TransactionHistoryEntry> accountOpenedEventEntry = TransactionHistoryEntry.handle((Event) TEST_ACCOUNT_OPENED_EVENT);
        Optional<TransactionHistoryEntry> paymentExecutedEventEntry = TransactionHistoryEntry.handle((Event) TEST_ACCOUNT_OPENED_EVENT);

        // then
        assertTrue(accountOpenedEventEntry.isPresent());
        assertTrue(paymentExecutedEventEntry.isPresent());
    }

    /**
     * Tests the handling of {@link AccountOpenedEvent}.
     * Verifies that the transaction history entry created for the event has the expected type, date, and amount.
     */
    @Test
    public void testHandleAccountOpenedEvent() {
        // when
        TransactionHistoryEntry entry = TransactionHistoryEntry.handle(TEST_ACCOUNT_OPENED_EVENT);

        // then
        assertEquals(TransactionType.INITIAL, entry.type());
        assertEquals(TEST_ACCOUNT_OPENED_EVENT.getDate(), entry.date());
        assertEquals(TEST_ACCOUNT_OPENED_EVENT.getInitialDeposit(), entry.amount());
    }

    /**
     * Tests the handling of {@link PaymentExecutedEvent} with positive amount.
     * Verifies that the transaction history entry created for the event has the expected type, date, and amount.
     */
    @Test
    public void testHandlePaymentExecutedEventCredit() {
        // when
        TransactionHistoryEntry entry = TransactionHistoryEntry.handle(TEST_PAYMENT_EXECUTED_EVENT_CREDIT);

        // then
        assertEquals(TransactionType.CREDIT, entry.type());
        assertEquals(TEST_PAYMENT_EXECUTED_EVENT_CREDIT.getDate(), entry.date());
        assertEquals(TEST_PAYMENT_EXECUTED_EVENT_CREDIT.getAmount(), entry.amount());
    }

    /**
     * Tests the handling of {@link PaymentExecutedEvent} with negative amount.
     * Verifies that the transaction history entry created for the event has the expected type, date, and amount.
     */
    @Test
    public void testHandlePaymentExecutedEventDebit() {
        // when
        TransactionHistoryEntry entry = TransactionHistoryEntry.handle(TEST_PAYMENT_EXECUTED_EVENT_DEBIT);

        // then
        assertEquals(TransactionType.DEBIT, entry.type());
        assertEquals(TEST_PAYMENT_EXECUTED_EVENT_DEBIT.getDate(), entry.date());
        assertEquals(TEST_PAYMENT_EXECUTED_EVENT_DEBIT.getAmount(), entry.amount());
    }
}