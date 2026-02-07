package io.github.sardul3.expense.adapter.in.web.controller;

import io.github.sardul3.expense.adapter.common.PrimaryAdapter;
import io.github.sardul3.expense.adapter.in.web.dto.AllExpenseGroupsResponse;
import io.github.sardul3.expense.application.dto.RetrieveExpenseGroupsResponse;
import io.github.sardul3.expense.application.port.in.RetrieveAllExpenseGroupsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PrimaryAdapter
@RestController
@RequestMapping("/api/v1/expense")
public class RetrieveAllExpenseGroupsController {
    private static final Logger log = LoggerFactory.getLogger(RetrieveAllExpenseGroupsController.class);

    private final RetrieveAllExpenseGroupsUseCase retrieveAllExpenseGroupsUseCase;

    public RetrieveAllExpenseGroupsController(RetrieveAllExpenseGroupsUseCase retrieveAllExpenseGroupsUseCase) {
        this.retrieveAllExpenseGroupsUseCase = retrieveAllExpenseGroupsUseCase;
    }

    @GetMapping(value ="/groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AllExpenseGroupsResponse>> getAllExpenseGroups() {
        log.info("Get All expense groups request");

        List<RetrieveExpenseGroupsResponse> allExpenseGroups = retrieveAllExpenseGroupsUseCase.getAllExpenseGroups();
        List<AllExpenseGroupsResponse> response = allExpenseGroups.stream()
                .map(entry -> new AllExpenseGroupsResponse(entry.name()))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
