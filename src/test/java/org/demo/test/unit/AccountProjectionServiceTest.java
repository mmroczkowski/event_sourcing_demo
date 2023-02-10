package org.demo.test.unit;

import org.demo.read.endpoint.account.model.TransactionHistoryEntry;
import org.demo.read.projection.account.AccountProjection;
import org.demo.read.projection.account.AccountProjectionRepository;
import org.demo.read.projection.account.AccountProjectionService;
import org.demo.write.aggregate.Event;
import org.demo.write.aggregate.account.event.AccountOpenedEvent;
import org.demo.write.aggregate.account.event.PaymentExecutedEvent;
import org.demo.write.eventstore.EventStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.demo.read.endpoint.account.model.TransactionType.CREDIT;
import static org.demo.read.endpoint.account.model.TransactionType.INITIAL;
import static org.demo.test.util.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for the {@link AccountProjectionService} class.
 *
 * @author mmroczkowski
 */
@ExtendWith(MockitoExtension.class)
public class AccountProjectionServiceTest {

    @Mock
    private EventStore eventStore;

    @Mock
    private AccountProjectionRepository accountProjectionRepository;

    @InjectMocks
    private AccountProjectionService accountProjectionService;

    /**
     * Verifies that the correct account projection is created and saved to the repository.
     */
    @Test
    public void shouldProjectAccountOpenEvent() {
        // given
        AccountOpenedEvent accountOpenedEvent = TEST_ACCOUNT_OPENED_EVENT;

        // when
        accountProjectionService.projectAccountOpenEvent(accountOpenedEvent);

        // then
        ArgumentCaptor<AccountProjection> argumentCaptor = ArgumentCaptor.forClass(AccountProjection.class);
        verify(accountProjectionRepository).save(argumentCaptor.capture());
        assertEquals(accountOpenedEvent.getAggregateId(), argumentCaptor.getValue().getId());
        assertEquals(accountOpenedEvent.getAccountNumber(), argumentCaptor.getValue().getAccountNumber());
        assertEquals(accountOpenedEvent.getInitialDeposit(), argumentCaptor.getValue().getBalance());
        assertEquals(accountOpenedEvent.getCreditLine(), argumentCaptor.getValue().getCreditLine());
    }

    /**
     * Verifies that the balance of the account projection is updated.
     */
    @Test
    public void shouldProjectPaymentExecutedEvent() {
        // given
        PaymentExecutedEvent paymentExecutedEvent = TEST_PAYMENT_EXECUTED_EVENT;
        AccountProjection accountProjection =
                new AccountProjection(TEST_UUID, "123456789", BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
        when(accountProjectionRepository.findById(paymentExecutedEvent.getAggregateId())).thenReturn(Optional.of(accountProjection));

        // when
        accountProjectionService.projectPaymentExecutedEvent(paymentExecutedEvent);

        // then
        assertEquals(BigDecimal.valueOf(1500), accountProjection.getBalance());
    }

    /**
     * Verifies that the correct list of account projections is returned.
     */
    @Test
    public void shouldGetAllAccounts() {
        // given
        AccountProjection accountProjection1 =
                new AccountProjection(UUID.randomUUID(), "123456789", BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
        AccountProjection accountProjection2 =
                new AccountProjection(UUID.randomUUID(), "987654321", BigDecimal.valueOf(2000), BigDecimal.valueOf(2000));
        List<AccountProjection> expectedAccountProjections = Arrays.asList(accountProjection1, accountProjection2);
        when(accountProjectionRepository.findAll()).thenReturn(expectedAccountProjections);

        // when
        List<AccountProjection> actualAccountProjections = accountProjectionService.getAllAccounts();

        // then
        assertEquals(expectedAccountProjections.stream().map(AccountProjection::getId).toList(),
                actualAccountProjections.stream().map(AccountProjection::getId).toList());
    }

    /**
     * Verifies that the correct list of transaction history entries is returned.
     */
    @Test
    public void shouldGetAccountHistory() {
        // given
        UUID accountId = UUID.randomUUID();
        Date sinceDate = new Date();
        Event accountOpenedEvent = new AccountOpenedEvent(UUID.randomUUID(), "123456789", BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
        Event paymentExecutedEvent = new PaymentExecutedEvent(UUID.randomUUID(), BigDecimal.valueOf(100));
        List<Event> expectedEvents = Arrays.asList(accountOpenedEvent, paymentExecutedEvent);
        when(eventStore.findAllEventsForAggregateSinceDate(accountId, sinceDate)).thenReturn(expectedEvents);

        // when
        List<TransactionHistoryEntry> actualTransactionHistory = accountProjectionService.getAccountHistory(accountId, sinceDate);

        // then
        assertEquals(2, actualTransactionHistory.size());

        TransactionHistoryEntry firstHistoryEntry = actualTransactionHistory.get(0);
        assertEquals(firstHistoryEntry.type(), INITIAL);
        assertEquals(firstHistoryEntry.amount(), BigDecimal.valueOf(1000));

        TransactionHistoryEntry secondHistoryEntry = actualTransactionHistory.get(1);
        assertEquals(secondHistoryEntry.type(), CREDIT);
        assertEquals(secondHistoryEntry.amount(), BigDecimal.valueOf(100));
    }
}
