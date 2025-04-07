package io.github.sardul3.expense.application.port.out;

import io.github.sardul3.expense.domain.model.ExpenseGroup;

public interface ExpenseGroupRepository {
    boolean existsByName(String name);
    ExpenseGroup save(ExpenseGroup expenseGroup);
}
