package io.github.sardul3.expense.adapter.out.persistence.inmemory;

import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.ExpenseGroupId;
import io.github.sardul3.expense.domain.valueobject.GroupName;

import java.util.*;

public class InMemoryExpenseGroupRepository implements ExpenseGroupRepository {

    private Map<ExpenseGroupId, ExpenseGroup> store = new HashMap<>();

    @Override
    public boolean existsByName(GroupName groupName) {
        return store.values().stream()
                .anyMatch(expenseGroup -> expenseGroup.getGroupName().equals(groupName));
    }

    @Override
    public ExpenseGroup save(ExpenseGroup expenseGroup) {
       store.put(expenseGroup.getId(), expenseGroup);
       return expenseGroup;
    }

    @Override
    public List<ExpenseGroup> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public Optional<ExpenseGroup> findById(UUID id) {
        if(Optional.ofNullable(store.get(id)).isPresent()) {
            return Optional.of(store.get(id));
        } else {
            return Optional.empty();
        }
    }
}
