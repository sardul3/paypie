package io.github.sardul3.expense.adapter.in.web.controller;

import io.github.sardul3.expense.adapter.common.PrimaryAdapter;
import io.github.sardul3.expense.application.dto.ExpenseHistoryPageResponse;
import io.github.sardul3.expense.application.port.in.GetExpenseHistoryUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@PrimaryAdapter
@RestController
@RequestMapping("/api/v1/expense")
public class GetExpenseHistoryController {

    private static final Logger log = LoggerFactory.getLogger(GetExpenseHistoryController.class);
    private static final int MAX_PAGE_SIZE = 100;

    private final GetExpenseHistoryUseCase getExpenseHistoryUseCase;

    public GetExpenseHistoryController(GetExpenseHistoryUseCase getExpenseHistoryUseCase) {
        this.getExpenseHistoryUseCase = getExpenseHistoryUseCase;
    }

    @GetMapping(value = "/groups/{id}/activities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseHistoryPageResponse> getExpenseHistory(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        int safeSize = Math.min(Math.max(1, size), MAX_PAGE_SIZE);
        int safePage = Math.max(0, page);
        log.info("Get expense history request: groupId={}, page={}, size={}", id, safePage, safeSize);
        ExpenseHistoryPageResponse response = getExpenseHistoryUseCase.getExpenseHistory(id, safePage, safeSize);
        return ResponseEntity.ok(response);
    }
}
