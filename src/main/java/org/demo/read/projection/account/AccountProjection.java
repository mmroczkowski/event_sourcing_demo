package org.demo.read.projection.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.demo.write.aggregate.account.event.PaymentExecutedEvent;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The AccountProjection class represents a projection of an Account in the relational database.
 *
 * @author mmroczkowski
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountProjection {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private BigDecimal creditLine;

    /**
     * The create method creates and returns an instance of the AccountProjection class with the provided information.
     *
     * @param id            the unique identifier of the account
     * @param accountNumber the account number of the account
     * @param balance       the current balance of the account
     * @param creditLine    the credit line of the account
     * @return the newly created AccountProjection object
     */
    public static AccountProjection create(UUID id, String accountNumber, BigDecimal balance, BigDecimal creditLine) {
        AccountProjection account = new AccountProjection();
        account.id = id;
        account.accountNumber = accountNumber;
        account.balance = balance;
        account.creditLine = creditLine;
        return account;
    }

    /**
     * The projectPaymentExecution method updates the balance of the AccountProjection object based on the executed
     * payment information in the PaymentExecutedEvent object.
     *
     * @param paymentExecutedEvent The PaymentExecutedEvent object which contains information about the executed payment
     */
    public void projectPaymentExecution(PaymentExecutedEvent paymentExecutedEvent) {
        balance = balance.add(paymentExecutedEvent.getAmount());
    }
}
