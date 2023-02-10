package org.demo.test.unit;

import org.demo.write.aggregate.account.event.AccountOpenedEvent;
import org.demo.write.aggregate.account.event.PaymentExecutedEvent;
import org.demo.write.aggregate.account.exception.InsufficientBalanceException;
import org.demo.write.eventstore.EventStore;
import org.demo.write.handler.AccountCommandHandler;
import org.demo.write.handler.command.ExecutePaymentCommand;
import org.demo.write.handler.command.OpenAccountCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.demo.test.util.TestData.TEST_UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This is a unit test class for {@link AccountCommandHandler}.
 *
 * @author mmroczkowski
 */
@ExtendWith(MockitoExtension.class)
public class AccountCommandHandlerTest {

    @Mock
    private EventStore eventStore;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    private AccountCommandHandler accountCommandHandler;

    @BeforeEach
    public void setUp() {
        accountCommandHandler = new AccountCommandHandler(eventStore, eventPublisher);
    }

    /**
     * Verifies that the method correctly saves and publishes {@link AccountOpenedEvent} event with the right data.
     */
    @Test
    public void testOpenAccount() {
        // given
        OpenAccountCommand openAccountCommand =
                new OpenAccountCommand("123", new BigDecimal(100), new BigDecimal(200));

        // when
        accountCommandHandler.openAccount(openAccountCommand);

        // then
        ArgumentCaptor<AccountOpenedEvent> argumentCaptorStore = ArgumentCaptor.forClass(AccountOpenedEvent.class);
        verify(eventStore).saveEvent(argumentCaptorStore.capture());
        assertEquals(openAccountCommand.accountNumber(), argumentCaptorStore.getValue().getAccountNumber());
        assertEquals(openAccountCommand.initialDeposit(), argumentCaptorStore.getValue().getInitialDeposit());
        assertEquals(openAccountCommand.creditLine(), argumentCaptorStore.getValue().getCreditLine());

        ArgumentCaptor<AccountOpenedEvent> argumentCaptorPublisher = ArgumentCaptor.forClass(AccountOpenedEvent.class);
        verify(eventPublisher).publishEvent(argumentCaptorPublisher.capture());
        assertEquals(openAccountCommand.accountNumber(), argumentCaptorPublisher.getValue().getAccountNumber());
        assertEquals(openAccountCommand.initialDeposit(), argumentCaptorPublisher.getValue().getInitialDeposit());
        assertEquals(openAccountCommand.creditLine(), argumentCaptorPublisher.getValue().getCreditLine());
    }

    /**
     * Verifies that the method correctly saves and publishes {@link PaymentExecutedEvent} event with the right data.
     */
    @Test
    public void testExecutePayment() throws InsufficientBalanceException {
        // given
        ExecutePaymentCommand executePaymentCommand = new ExecutePaymentCommand(TEST_UUID, new BigDecimal(50));
        when(eventStore.findAllEventsForAggregate(TEST_UUID)).thenReturn(List.of(
                new AccountOpenedEvent(UUID.randomUUID(), "123456789", BigDecimal.valueOf(1000), BigDecimal.valueOf(1000))));

        // when
        accountCommandHandler.executePayment(executePaymentCommand);

        // then
        ArgumentCaptor<PaymentExecutedEvent> argumentCaptorStore = ArgumentCaptor.forClass(PaymentExecutedEvent.class);
        verify(eventStore).saveEvent(argumentCaptorStore.capture());
        assertEquals(executePaymentCommand.accountId(), argumentCaptorStore.getValue().getAggregateId());
        assertEquals(executePaymentCommand.amount(), argumentCaptorStore.getValue().getAmount());

        ArgumentCaptor<PaymentExecutedEvent> argumentCaptorPublisher = ArgumentCaptor.forClass(PaymentExecutedEvent.class);
        verify(eventPublisher).publishEvent(argumentCaptorPublisher.capture());
        assertEquals(executePaymentCommand.accountId(), argumentCaptorPublisher.getValue().getAggregateId());
        assertEquals(executePaymentCommand.amount(), argumentCaptorPublisher.getValue().getAmount());
    }
}