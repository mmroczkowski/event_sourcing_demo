package org.demo.write.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Base class representing an aggregate in the system.
 *
 * @author mmroczkowski
 */
@Getter
@AllArgsConstructor
public abstract class Aggregate {
    protected UUID id;

    /**
     * Handles a generalized event, by choosing specific handler.
     *
     * @param event Event to be handled.
     * @throws BusinessException If an error occurs while handling the event.
     */
    public abstract void handle(Event event) throws BusinessException;
}
