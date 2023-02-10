package org.demo.write.handler.command;

import java.math.BigDecimal;

/**
 * A command that represents a request to open a new account.
 *
 * @author mmroczkowski
 */
public record OpenAccountCommand(
        String accountNumber,
        BigDecimal initialDeposit,
        BigDecimal creditLine) {
}
