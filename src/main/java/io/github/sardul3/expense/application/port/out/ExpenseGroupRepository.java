package io.github.sardul3.expense.application.port.out;

import io.github.sardul3.expense.application.common.annotation.OutputPort;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.valueobject.GroupName;

import java.util.List;

@OutputPort(
        description = "Abstracts the persistence of Expense Groups",
        role = OutputPort.Role.PERSISTENCE
)
public interface ExpenseGroupRepository {
    boolean existsByName(GroupName groupName);
    ExpenseGroup save(ExpenseGroup expenseGroup);
    List<ExpenseGroup> findAll();
}
