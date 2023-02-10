package org.demo.write.endpoint;

import lombok.RequiredArgsConstructor;
import org.demo.write.aggregate.account.exception.InsufficientBalanceException;
import org.demo.write.handler.AccountCommandHandler;
import org.demo.write.handler.command.ExecutePaymentCommand;
import org.demo.write.handler.command.OpenAccountCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST controller class that handles HTTP requests related to account commands.
 *
 * @author mmroczkowski
 */
@RestController
@RequiredArgsConstructor
public class AccountCommandEndpoint {

    private final AccountCommandHandler accountCommandHandler;

    /**
     * Endpoint for opening an account.
     *
     * @param openAccountCommand The command to open the account.
     * @return A ResponseEntity containing a message.
     */
    @PostMapping("/openAccount")
    public ResponseEntity<String> openAccount(@RequestBody OpenAccountCommand openAccountCommand) {
        accountCommandHandler.openAccount(openAccountCommand);
        return ResponseEntity.ok("Account successfully opened.");
    }

    /**
     * Endpoint for executing a payment.
     * If the account does not have sufficient balance, a ResponseEntity with a bad request status
     * code and a message indicating insufficient balance will be returned.
     *
     * @param executePaymentCommand The command to execute the payment.
     * @return A ResponseEntity containing a message.
     */
    @PostMapping("/executePayment")
    public ResponseEntity<String> executePayment(@RequestBody ExecutePaymentCommand executePaymentCommand) {
        try {
            accountCommandHandler.executePayment(executePaymentCommand);
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body("Insufficient balance on account with id: " + executePaymentCommand.accountId());
        }
        return ResponseEntity.ok("Payment successfully executed.");
    }
}
