package org.demo.test.integration;

import org.demo.write.handler.command.ExecutePaymentCommand;
import org.demo.write.handler.command.OpenAccountCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.demo.test.util.JSONUtils.asJsonString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class contains integration tests for the AccountCommandEndpoint class.
 *
 * @author mmroczkowski
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AccountCommandEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifies the opening of a new account by sending a POST request to the "/openAccount" endpoint.
     */
    @Test
    public void testOpenAccount() throws Exception {
        OpenAccountCommand openAccountCommand = new OpenAccountCommand("12345", new BigDecimal(200), new BigDecimal(200));

        mockMvc.perform(post("/openAccount")
                        .content(asJsonString(openAccountCommand))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Account successfully opened."));
    }

    /**
     * Verifies a successful payment execution by sending a POST request to the "/executePayment" endpoint.
     */
    @Test
    public void testExecutePayment() throws Exception {
        ExecutePaymentCommand executePaymentCommand =
                new ExecutePaymentCommand(UUID.fromString("123e4567-e89b-12d3-a456-426655440000"), new BigDecimal(200));

        mockMvc.perform(post("/executePayment")
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(executePaymentCommand)))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment successfully executed."));
    }

    /**
     * Verifies a failed payment execution due to insufficient funds.
     */
    @Test
    public void testExecutePaymentWithInsufficientBalance() throws Exception {
        ExecutePaymentCommand executePaymentCommand =
                new ExecutePaymentCommand(UUID.fromString("456e4567-e89b-12d3-a456-426655441111"), new BigDecimal(-10000));

        mockMvc.perform(post("/executePayment")
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(executePaymentCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient balance on account with id: " + executePaymentCommand.accountId()));
    }
}
