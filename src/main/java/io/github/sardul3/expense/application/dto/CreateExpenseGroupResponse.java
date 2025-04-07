package io.github.sardul3.expense.application.dto;

import io.github.sardul3.expense.domain.valueobject.ExpenseGroupId;
import io.github.sardul3.expense.domain.valueobject.GroupName;

import java.util.UUID;

public record CreateExpenseGroupResponse(UUID id, String name) {
    public CreateExpenseGroupResponse(ExpenseGroupId id, GroupName groupName) {
        this(id.getId(), groupName.getName());
    }
}
