package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.domain.model.ExpenseGroup;

public interface CreateExpenseGroupUseCase {
    ExpenseGroup createExpenseGroup(CreateExpenseGroupCommand CreateExpenseGroupCommand);
}
