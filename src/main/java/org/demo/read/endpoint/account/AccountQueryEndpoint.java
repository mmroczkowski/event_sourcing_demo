package org.demo.read.endpoint.account;

import lombok.RequiredArgsConstructor;
import org.demo.read.endpoint.account.model.TransactionHistoryEntry;
import org.demo.read.projection.account.AccountProjection;
import org.demo.read.projection.account.AccountProjectionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A REST controller class that handles HTTP requests related to account queries.
 *
 * @author mmroczkowski
 */
@RestController
@RequiredArgsConstructor
public class AccountQueryEndpoint {

    private final AccountProjectionService projectionService;

    /**
     * Returns a list of all accounts.
     *
     * @return a list of {@link AccountProjection} objects.
     */
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountProjection>> accounts() {
        return ResponseEntity.ok(projectionService.getAllAccounts());
    }

    /**
     * Returns a list of transaction history entries for a specified account and date range.
     *
     * @param accountId the {@link UUID} identifier of the account for which to retrieve transaction history.
     * @param sinceDate the starting date for the transaction history search.
     * @return a list of {@link TransactionHistoryEntry} objects.
     */
    @GetMapping("/transactionHistory")
    public ResponseEntity<List<TransactionHistoryEntry>> transactionHistory(
            @RequestParam UUID accountId, @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date sinceDate) {
        return ResponseEntity.ok(projectionService.getAccountHistory(accountId, sinceDate));
    }
}
