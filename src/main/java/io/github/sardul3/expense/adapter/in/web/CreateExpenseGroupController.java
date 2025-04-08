package io.github.sardul3.expense.adapter.in.web;

import io.github.sardul3.expense.application.dto.CreateExpenseGroupCommand;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CreateExpenseGroupController {

    private final CreateExpenseGroupUseCase createExpenseGroupUseCase;

    public CreateExpenseGroupController(CreateExpenseGroupUseCase createExpenseGroupUseCase) {
        this.createExpenseGroupUseCase = createExpenseGroupUseCase;
    }


    public ResponseEntity<String> createExpenseGroup(ExpenseGroup expenseGroup) {
        createExpenseGroupUseCase.createExpenseGroup(new CreateExpenseGroupCommand(null, null));
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseGroup.toString());
    }
}
