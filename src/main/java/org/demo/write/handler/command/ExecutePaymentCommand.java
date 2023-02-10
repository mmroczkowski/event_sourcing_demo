package org.demo.write.handler.command;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * A value object representing the command to execute a payment.
 *
 * @author mmroczkowski
 */
public record ExecutePaymentCommand(
        UUID accountId,
        BigDecimal amount) {
}
