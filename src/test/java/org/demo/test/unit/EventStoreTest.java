package org.demo.test.unit;

import org.demo.write.aggregate.Event;
import org.demo.write.eventstore.EventStore;
import org.demo.write.eventstore.PersistentEvent;
import org.demo.write.eventstore.PersistentEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.demo.test.util.TestData.TEST_UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link EventStore}.
 *
 * @author mmroczkowski
 */
@ExtendWith(MockitoExtension.class)
public class EventStoreTest {

    public static final PersistentEvent TEST_PERSISTENT_EVENT1 = new PersistentEvent(0L, UUID.randomUUID(),
            "org.demo.write.aggregate.account.event.PaymentExecutedEvent", """
            { "aggregateId": "456e4567-e89b-12d3-a456-426655441111", "date":"2022-02-01 12:00:00.843", "amount": 1000 }
            """, new Date());

    public static final PersistentEvent TEST_PERSISTENT_EVENT2 = new PersistentEvent(1L, UUID.randomUUID(),
            "org.demo.write.aggregate.account.event.PaymentExecutedEvent", """
            { "aggregateId": "456e4567-e89b-12d3-a456-426655441111", "date":"2022-02-01 12:00:00.843", "amount": 1000 }
            """, new Date());

    @Mock
    private PersistentEventRepository persistentEventRepository;

    @InjectMocks
    private EventStore eventStore;

    /**
     * Verifies that the {@link PersistentEventRepository#save(Object)} method is called with the appropriate arguments.
     */
    @Test
    public void testSaveEvent() {
        // given
        Event event = new Event() {
        };

        // when
        eventStore.saveEvent(event);

        // then
        verify(persistentEventRepository).save(any(PersistentEvent.class));
    }

    /**
     * Verifies that the {@link PersistentEventRepository#findByAggregateId(UUID)} method is called with the appropriate arguments,
     * and that the correct number of events is returned.
     */
    @Test
    public void testFindAllEventsForAggregate() {
        // when
        when(persistentEventRepository.findByAggregateId(TEST_UUID)).thenReturn(Arrays.asList(TEST_PERSISTENT_EVENT1, TEST_PERSISTENT_EVENT2));

        // then
        List<Event> events = eventStore.findAllEventsForAggregate(TEST_UUID);
        assertEquals(2, events.size());
    }

    /**
     * Verifies that the {@link PersistentEventRepository#findByAggregateIdAndCreateDateAfter(UUID, Date)} method is called with the appropriate arguments,
     * and that the correct number of events is returned.
     */
    @Test
    public void testFindAllEventsForAggregateSinceDate() {
        // given
        Date sinceDate = new Date();

        // when
        when(persistentEventRepository.findByAggregateIdAndCreateDateAfter(TEST_UUID, sinceDate))
                .thenReturn(Arrays.asList(TEST_PERSISTENT_EVENT1, TEST_PERSISTENT_EVENT2));

        // then
        List<Event> events = eventStore.findAllEventsForAggregateSinceDate(TEST_UUID, sinceDate);
        assertEquals(2, events.size());
    }
}
