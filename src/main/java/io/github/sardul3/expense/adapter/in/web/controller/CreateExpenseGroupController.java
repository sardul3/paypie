package io.github.sardul3.expense.adapter.in.web.controller;

import io.github.sardul3.expense.adapter.common.PrimaryAdapter;
import io.github.sardul3.expense.adapter.in.web.dto.CreateExpenseGroupRequest;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@PrimaryAdapter
@RestController
@RequestMapping("/api/v1/expense")
public class CreateExpenseGroupController {

    private final CreateExpenseGroupUseCase createExpenseGroupUseCase;

    public CreateExpenseGroupController(CreateExpenseGroupUseCase createExpenseGroupUseCase) {
        this.createExpenseGroupUseCase = createExpenseGroupUseCase;
    }


    @PostMapping("/groups")
    public ResponseEntity<CreateExpenseGroupResponse> createExpenseGroup(@Valid @RequestBody CreateExpenseGroupRequest request) {
        CreateExpenseGroupResponse response =
                createExpenseGroupUseCase.createExpenseGroup(
                        new CreateExpenseGroupCommand(
                                request.name(), request.createdBy()
                        )
                );
        return ResponseEntity.created(URI.create("/api/v1/expense/groups/" + response.id()))
                .body(response);    }
}
