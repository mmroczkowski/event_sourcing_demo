package org.demo.write.eventstore;

import lombok.AllArgsConstructor;
import org.demo.write.aggregate.Event;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * The {@code EventStore} is a service class responsible for storing and retrieving events in a persistent storage.
 *
 * @author mmroczkowski
 */
@Service
@Transactional
@AllArgsConstructor
public class EventStore {

    private final PersistentEventRepository persistentEventRepository;

    /**
     * Saves an event to the underlying storage.
     *
     * @param event The event to be saved.
     */
    public void saveEvent(Event event) {
        persistentEventRepository.save(PersistentEvent.serialize(event));
    }

    /**
     * Retrieves all events for a specific aggregate.
     *
     * @param aggregateId The id of the aggregate.
     * @return A list of events associated with the aggregate.
     */
    public List<Event> findAllEventsForAggregate(UUID aggregateId) {
        return persistentEventRepository.findByAggregateId(aggregateId).stream().map(PersistentEvent::deserialize).toList();
    }

    /**
     * Retrieves all events for a specific aggregate since a specific date.
     *
     * @param aggregateId The id of the aggregate.
     * @param sinceDate   The date since which events should be retrieved.
     * @return A list of events associated with the aggregate since the specified date.
     */
    public List<Event> findAllEventsForAggregateSinceDate(UUID aggregateId, Date sinceDate) {
        return persistentEventRepository.findByAggregateIdAndCreateDateAfter(aggregateId, sinceDate).stream().map(PersistentEvent::deserialize).toList();
    }
}
