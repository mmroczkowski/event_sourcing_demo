package org.demo.write.handler;

import lombok.AllArgsConstructor;
import org.demo.write.aggregate.account.Account;
import org.demo.write.aggregate.Aggregate;
import org.demo.write.aggregate.account.event.AccountOpenedEvent;
import org.demo.write.aggregate.Event;
import org.demo.write.aggregate.account.event.PaymentExecutedEvent;
import org.demo.write.aggregate.account.exception.InsufficientBalanceException;
import org.demo.write.eventstore.EventStore;
import org.demo.write.handler.command.ExecutePaymentCommand;
import org.demo.write.handler.command.OpenAccountCommand;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * The AccountCommandHandler class is responsible for handling account related commands such as opening an account and executing payments.
 *
 * @author mmroczkowski
 */
@Service
@Transactional
@AllArgsConstructor
public class AccountCommandHandler {

    private final EventStore eventStore;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * The openAccount method creates an instance of AccountOpenedEvent, saves it to the event store and publishes it.
     *
     * @param openAccountCommand the OpenAccountCommand object which contains information about the new account
     */
    public void openAccount(OpenAccountCommand openAccountCommand) {
        AccountOpenedEvent accountOpenedEvent = new AccountOpenedEvent(
                UUID.randomUUID(),
                openAccountCommand.accountNumber(),
                openAccountCommand.initialDeposit(),
                openAccountCommand.creditLine());

        eventStore.saveEvent(accountOpenedEvent);
        eventPublisher.publishEvent(accountOpenedEvent);
    }

    /**
     * The executePayment method creates an instance of PaymentExecutedEvent, saves it to the event store, and publishes it.
     *
     * @param executePaymentCommand The ExecutePaymentCommand object which contains information about the payment
     * @throws InsufficientBalanceException when there is not enough balance in the account to execute the payment
     */
    public void executePayment(ExecutePaymentCommand executePaymentCommand) throws InsufficientBalanceException {
        PaymentExecutedEvent paymentExecutedEvent = new PaymentExecutedEvent(
                executePaymentCommand.accountId(),
                executePaymentCommand.amount());

        Account account = rebuildAggregate(executePaymentCommand.accountId(), Account.class);
        account.handle(paymentExecutedEvent);

        eventStore.saveEvent(paymentExecutedEvent);
        eventPublisher.publishEvent(paymentExecutedEvent);
    }

    /**
     * The rebuildAggregate method finds all events related to the aggregate and uses them to recreate aggregate object.
     *
     * @param uuid the unique identifier of the aggregate
     * @return the reconstructed aggregate
     */
    private <T extends Aggregate> T rebuildAggregate(UUID uuid, Class<T> cls) {
        List<Event> events = eventStore.findAllEventsForAggregate(uuid);
        T aggregate;
        try {
            aggregate = cls.getConstructor(UUID.class).newInstance(uuid);
            for (Event event : events) {
                aggregate.handle(event);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during rebuilding of aggregate: " + uuid, e);
        }
        return aggregate;
    }
}
