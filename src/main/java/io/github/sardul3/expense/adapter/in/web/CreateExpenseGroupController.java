package io.github.sardul3.expense.adapter.in.web;

import io.github.sardul3.expense.domain.model.ExpenseGroup;
import org.springframework.http.ResponseEntity;

public class CreateExpenseGroupController {

    public ResponseEntity<String> createExpenseGroup(ExpenseGroup expenseGroup) {
        return ResponseEntity.ok("MOCK OK");
    }
}
