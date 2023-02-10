package org.demo.write.aggregate;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * Base class representing an event in the system.
 *
 * @author mmroczkowski
 */
@Getter
@NoArgsConstructor
public abstract class Event {
    protected UUID aggregateId;
    protected Date date;

    public Event(UUID aggregateId) {
        this.aggregateId = aggregateId;
        this.date = new Date();
    }
}
