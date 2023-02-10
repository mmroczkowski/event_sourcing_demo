package org.demo.write.aggregate.account;

import lombok.Getter;
import org.demo.write.aggregate.Aggregate;
import org.demo.write.aggregate.Event;
import org.demo.write.aggregate.account.event.AccountOpenedEvent;
import org.demo.write.aggregate.account.event.PaymentExecutedEvent;
import org.demo.write.aggregate.BusinessException;
import org.demo.write.aggregate.account.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Class representing an Account aggregate.
 *
 * @author mmroczkowski
 */
@Getter
public class Account extends Aggregate {

    private String accountNumber;
    private BigDecimal balance;
    private BigDecimal creditLine;

    public Account(UUID id) {
        super(id);
    }

    /**
     * Handles a generalized event, by choosing specific handler.
     *
     * @param event Event to be handled.
     * @throws BusinessException If an error occurs while handling the event.
     * @throws RuntimeException  If there is a problem with handler implementation.
     */
    @Override
    public void handle(Event event) throws BusinessException {
        // this structure is very fragile to future changes
        // can be fixed by reflective call or by using specialized libraries
        if (event instanceof AccountOpenedEvent accountOpenedEvent) {
            handle(accountOpenedEvent);
        } else if (event instanceof PaymentExecutedEvent paymentExecutedEvent) {
            handle(paymentExecutedEvent);
        } else {
            throw new RuntimeException("Cannot find handler method for event: " + event.getClass().getName());
        }
    }

    /**
     * Handles an AccountOpenedEvent.
     *
     * @param accountOpenedEvent AccountOpenedEvent to be handled.
     */
    public void handle(AccountOpenedEvent accountOpenedEvent) {
        accountNumber = accountOpenedEvent.getAccountNumber();
        balance = accountOpenedEvent.getInitialDeposit();
        creditLine = accountOpenedEvent.getCreditLine();
    }

    /**
     * Handles a PaymentExecutedEvent.
     *
     * @param paymentExecutedEvent PaymentExecutedEvent to be handled.
     * @throws InsufficientBalanceException If the Account's balance taking credit line into consideration is not sufficient.
     */
    public void handle(PaymentExecutedEvent paymentExecutedEvent) throws InsufficientBalanceException {
        balance = balance.add(paymentExecutedEvent.getAmount());
        if (balance.add(creditLine).compareTo(BigDecimal.ZERO) < 1) {
            throw new InsufficientBalanceException();
        }
    }
}
