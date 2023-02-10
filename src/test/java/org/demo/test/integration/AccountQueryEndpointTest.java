package org.demo.test.integration;

import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class contains integration tests for the AccountQueryEndpoint class.
 *
 * @author mmroczkowski
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AccountQueryEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifies that /accounts endpoint returns test accounts.
     */
    @Test
    void testAccountsEndpoint() throws Exception {
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().string(new StringContains("123e4567-e89b-12d3-a456-426655440000")))
                .andExpect(content().string(new StringContains("A12345")))
                .andExpect(content().string(new StringContains("456e4567-e89b-12d3-a456-426655441111")))
                .andExpect(content().string(new StringContains("B67890")));
    }

    /**
     * Verifies that /transactionHistory endpoint returns correct history for test account.
     */
    @Test
    void testTransactionHistoryEndpoint() throws Exception {
        String result = mockMvc.perform(get("/transactionHistory")
                        .param("accountId", "123e4567-e89b-12d3-a456-426655440000")
                        .param("sinceDate", "01/01/2020"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertTrue(result.contains("INITIAL"));
        assertTrue(result.contains("DEBIT"));
    }

    /**
     * Verifies that /transactionHistory endpoint returns correct history for test account constrained by date.
     */
    @Test
    void testTransactionHistoryEndpointWithDateConstraint() throws Exception {
        String result = mockMvc.perform(get("/transactionHistory")
                        .param("accountId", "123e4567-e89b-12d3-a456-426655440000")
                        .param("sinceDate", "02/01/2022"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertTrue(result.contains("DEBIT"));
        assertFalse(result.contains("INITIAL"));
    }
}
