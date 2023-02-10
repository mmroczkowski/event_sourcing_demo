package org.demo.write.aggregate.account.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.demo.write.aggregate.Event;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Class representing the event of an account being opened.
 *
 * @author mmroczkowski
 */
@Getter
@NoArgsConstructor
public class AccountOpenedEvent extends Event {
    private String accountNumber;
    private BigDecimal initialDeposit;
    private BigDecimal creditLine;

    public AccountOpenedEvent(UUID aggregateId, String AccountNumber, BigDecimal initialDeposit, BigDecimal creditLine) {
        super(aggregateId);
        this.accountNumber = AccountNumber;
        this.initialDeposit = initialDeposit;
        this.creditLine = creditLine;
    }
}
