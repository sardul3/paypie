package io.github.sardul3.expense.adapter.out.persistence;

import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.valueobject.ExpenseGroupId;

import java.util.HashMap;
import java.util.Map;

public class InMemoryExpenseGroupRepository implements ExpenseGroupRepository {

    private Map<ExpenseGroupId, ExpenseGroup> store = new HashMap<>();

    @Override
    public boolean existsByName(String name) {
        return store.values().stream()
                .anyMatch(expenseGroup -> expenseGroup.getGroupName().getName().equals(name));
    }

    @Override
    public ExpenseGroup save(ExpenseGroup expenseGroup) {
       store.put(expenseGroup.getId(), expenseGroup);
       return expenseGroup;
    }
}
