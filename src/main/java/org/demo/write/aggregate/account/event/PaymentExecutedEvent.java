package org.demo.write.aggregate.account.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.demo.write.aggregate.Event;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Class representing the event of a payment being executed.
 *
 * @author mmroczkowski
 */
@Getter
@NoArgsConstructor
public class PaymentExecutedEvent extends Event {
    private BigDecimal amount;

    public PaymentExecutedEvent(UUID aggregateId, BigDecimal amount) {
        super(aggregateId);
        this.amount = amount;
    }
}
