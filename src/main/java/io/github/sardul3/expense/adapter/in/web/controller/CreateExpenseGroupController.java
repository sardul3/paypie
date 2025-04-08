package io.github.sardul3.expense.adapter.in.web.controller;

import io.github.sardul3.expense.adapter.in.web.dto.CreateExpenseGroupRequest;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expense")
public class CreateExpenseGroupController {

    private final CreateExpenseGroupUseCase createExpenseGroupUseCase;

    public CreateExpenseGroupController(CreateExpenseGroupUseCase createExpenseGroupUseCase) {
        this.createExpenseGroupUseCase = createExpenseGroupUseCase;
    }


    @PostMapping("/groups")
    public ResponseEntity<CreateExpenseGroupResponse> createExpenseGroup(@RequestBody CreateExpenseGroupRequest request) {
        CreateExpenseGroupResponse response =
                createExpenseGroupUseCase.createExpenseGroup(
                        new CreateExpenseGroupCommand(
                                request.name(), request.createdBy()
                        )
                );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
