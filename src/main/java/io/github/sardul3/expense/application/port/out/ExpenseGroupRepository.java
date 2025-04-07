package io.github.sardul3.expense.application.port.out;

import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.valueobject.GroupName;

public interface ExpenseGroupRepository {
    boolean existsByName(GroupName groupName);
    ExpenseGroup save(ExpenseGroup expenseGroup);
}
