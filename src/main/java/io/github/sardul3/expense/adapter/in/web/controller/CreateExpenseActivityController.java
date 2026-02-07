package io.github.sardul3.expense.adapter.in.web.controller;

import io.github.sardul3.expense.adapter.common.PrimaryAdapter;
import io.github.sardul3.expense.adapter.in.web.dto.CreateExpenseActivityRequest;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityResponse;
import io.github.sardul3.expense.application.port.in.CreateExpenseActivityUseCase;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@PrimaryAdapter
@RestController
@RequestMapping("/api/v1/expense")
public class CreateExpenseActivityController {

    private static final Logger log = LoggerFactory.getLogger(CreateExpenseActivityController.class);

    private final CreateExpenseActivityUseCase createExpenseActivityUseCase;

    public CreateExpenseActivityController(CreateExpenseActivityUseCase createExpenseActivityUseCase) {
        this.createExpenseActivityUseCase = createExpenseActivityUseCase;
    }

    @PostMapping(value = "/groups/{groupId}/activities", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateExpenseActivityResponse> createExpenseActivity(
            @PathVariable UUID groupId,
            @Valid @RequestBody CreateExpenseActivityRequest request) {
        log.info("Create expense activity request: groupId={}, request={}", groupId, request);
        CreateExpenseActivityCommand command = new CreateExpenseActivityCommand(
                groupId,
                request.description(),
                request.amount(),
                request.paidBy(),
                request.splitWith()
        );
        CreateExpenseActivityResponse response = createExpenseActivityUseCase.createExpenseActivity(command);
        return ResponseEntity.created(URI.create("/api/v1/expense/groups/" + groupId + "/activities"))
                .body(response);
    }
}
