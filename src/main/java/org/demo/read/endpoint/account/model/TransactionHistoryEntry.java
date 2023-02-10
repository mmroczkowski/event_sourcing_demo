package org.demo.read.endpoint.account.model;

import org.demo.write.aggregate.account.event.AccountOpenedEvent;
import org.demo.write.aggregate.Event;
import org.demo.write.aggregate.account.event.PaymentExecutedEvent;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * Record representing a transaction history entry.
 *
 * @author mmroczkowski
 */
public record TransactionHistoryEntry(TransactionType type, Date date, BigDecimal amount) {

    /**
     * Returns a TransactionHistoryEntry for the given event.
     *
     * @param event Event to be handled.
     * @return A TransactionHistoryEntry for the given event if it can be handled, empty otherwise.
     */
    public static Optional<TransactionHistoryEntry> handle(Event event) {
        if (event instanceof AccountOpenedEvent accountOpenedEvent) {
            return Optional.of(handle(accountOpenedEvent));
        } else if (event instanceof PaymentExecutedEvent paymentExecutedEvent) {
            return Optional.of(handle(paymentExecutedEvent));
        }
        return Optional.empty();
    }

    /**
     * Returns a TransactionHistoryEntry for the given AccountOpenedEvent.
     *
     * @param accountOpenedEvent AccountOpenedEvent to be handled.
     * @return A TransactionHistoryEntry for the given AccountOpenedEvent.
     */
    public static TransactionHistoryEntry handle(AccountOpenedEvent accountOpenedEvent) {
        return new TransactionHistoryEntry(TransactionType.INITIAL, accountOpenedEvent.getDate(), accountOpenedEvent.getInitialDeposit());
    }

    /**
     * Returns a TransactionHistoryEntry for the given PaymentExecutedEvent.
     *
     * @param paymentExecutedEvent PaymentExecutedEvent to be handled.
     * @return A TransactionHistoryEntry for the given PaymentExecutedEvent.
     */
    public static TransactionHistoryEntry handle(PaymentExecutedEvent paymentExecutedEvent) {
        BigDecimal amount = paymentExecutedEvent.getAmount();
        return new TransactionHistoryEntry(amount.compareTo(BigDecimal.ZERO) > 0 ? TransactionType.CREDIT : TransactionType.DEBIT,
                paymentExecutedEvent.getDate(), amount);
    }
}
