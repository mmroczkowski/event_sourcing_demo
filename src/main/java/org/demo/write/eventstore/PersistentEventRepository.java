package org.demo.write.eventstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A repository to persist {@link PersistentEvent} entities in the database.
 *
 * @author mmroczkowski
 */
@Repository
public interface PersistentEventRepository extends JpaRepository<PersistentEvent, Long> {

    /**
     * Finds all the {@link PersistentEvent} entities associated with the given aggregate identifier.
     *
     * @param aggregateId the identifier of the aggregate for which to find all events.
     * @return a list of {@link PersistentEvent} entities associated with the given aggregate identifier.
     */
    List<PersistentEvent> findByAggregateId(UUID aggregateId);

    /**
     * Finds all the {@link PersistentEvent} entities associated with the given aggregate identifier and created after the specified date.
     *
     * @param aggregateId the identifier of the aggregate for which to find all events.
     * @param sinceDate   the date after which to find all events.
     * @return a list of {@link PersistentEvent} entities associated with the given aggregate identifier and created after the specified date.
     */
    List<PersistentEvent> findByAggregateIdAndCreateDateAfter(UUID aggregateId, Date sinceDate);
}
