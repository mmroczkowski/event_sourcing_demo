package org.demo.read.projection.account;

import lombok.AllArgsConstructor;
import org.demo.write.aggregate.account.event.AccountOpenedEvent;
import org.demo.write.aggregate.Event;
import org.demo.write.aggregate.account.event.PaymentExecutedEvent;
import org.demo.read.endpoint.account.model.TransactionHistoryEntry;
import org.demo.write.eventstore.EventStore;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The ProjectionService class is responsible for projecting events into account projection in relational database.
 *
 * @author mmroczkowski
 */
@Service
@Transactional
@AllArgsConstructor
public class AccountProjectionService {

    private final EventStore eventStore;
    private final AccountProjectionRepository accountProjectionRepository;

    /**
     * Subscribes to the AccountOpenedEvent and projects the event into an AccountProjection object.
     *
     * @param accountOpenedEvent The event that triggered the projection
     */
    @EventListener
    public void projectAccountOpenEvent(AccountOpenedEvent accountOpenedEvent) {
        accountProjectionRepository.save(AccountProjection.create(
                accountOpenedEvent.getAggregateId(),
                accountOpenedEvent.getAccountNumber(),
                accountOpenedEvent.getInitialDeposit(),
                accountOpenedEvent.getCreditLine()));
    }

    /**
     * Subscribes to the PaymentExecutedEvent and projects the event into an AccountProjection object.
     *
     * @param paymentExecutedEvent The event that triggered the projection
     */
    @EventListener
    public void projectPaymentExecutedEvent(PaymentExecutedEvent paymentExecutedEvent) {
        AccountProjection accountProjection = accountProjectionRepository.findById(paymentExecutedEvent.getAggregateId())
                .orElseThrow(() -> new RuntimeException("Cannot find projection of aggregate: " + paymentExecutedEvent.getAggregateId()));

        accountProjection.projectPaymentExecution(paymentExecutedEvent);
    }

    /**
     * Retrieves all AccountProjections from the repository.
     *
     * @return A list of all AccountProjections in the repository
     */
    public List<AccountProjection> getAllAccounts() {
        return accountProjectionRepository.findAll();
    }

    /**
     * Retrieves a history of transactions for a particular account since a certain date.
     *
     * @param accountId The ID of the account to retrieve the history for
     * @param sinceDate The date to retrieve the history from
     * @return A list of TransactionHistoryEntries representing the transaction history for the specified account
     */
    public List<TransactionHistoryEntry> getAccountHistory(UUID accountId, Date sinceDate) {
        List<Event> events = eventStore.findAllEventsForAggregateSinceDate(accountId, sinceDate);
        return events.stream().map(TransactionHistoryEntry::handle).filter(Optional::isPresent).map(Optional::get).toList();
    }
}
