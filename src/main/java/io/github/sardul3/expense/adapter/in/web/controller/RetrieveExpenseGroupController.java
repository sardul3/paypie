package io.github.sardul3.expense.adapter.in.web.controller;

import io.github.sardul3.expense.adapter.common.PrimaryAdapter;
import io.github.sardul3.expense.application.dto.ExpenseGroupDetailResponse;
import io.github.sardul3.expense.application.port.in.RetrieveExpenseGroupUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@PrimaryAdapter
@RestController
@RequestMapping("/api/v1/expense")
public class RetrieveExpenseGroupController {

    private static final Logger log = LoggerFactory.getLogger(RetrieveExpenseGroupController.class);

    private final RetrieveExpenseGroupUseCase retrieveExpenseGroupUseCase;

    public RetrieveExpenseGroupController(RetrieveExpenseGroupUseCase retrieveExpenseGroupUseCase) {
        this.retrieveExpenseGroupUseCase = retrieveExpenseGroupUseCase;
    }

    @GetMapping(value = "/groups/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExpenseGroupDetailResponse> getExpenseGroup(@PathVariable UUID id) {
        log.info("Get expense group request: id={}", id);
        ExpenseGroupDetailResponse response = retrieveExpenseGroupUseCase.getExpenseGroup(id);
        return ResponseEntity.ok(response);
    }
}
