package io.github.sardul3.expense.adapter.in.web;

import io.github.sardul3.expense.application.dto.CreateExpenseGroupCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CreateExpenseGroupController {

    private final CreateExpenseGroupUseCase createExpenseGroupUseCase;

    public CreateExpenseGroupController(CreateExpenseGroupUseCase createExpenseGroupUseCase) {
        this.createExpenseGroupUseCase = createExpenseGroupUseCase;
    }


    @PostMapping("/expense/group")
    public ResponseEntity<CreateExpenseGroupResponse> createExpenseGroup(@RequestBody ExpenseGroup expenseGroup) {
        CreateExpenseGroupResponse response =
                createExpenseGroupUseCase.createExpenseGroup(
                        new CreateExpenseGroupCommand(expenseGroup.getGroupName().getName(), expenseGroup.getGroupCreator().getEmail()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
