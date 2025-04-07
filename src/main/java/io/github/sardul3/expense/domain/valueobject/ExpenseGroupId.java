package io.github.sardul3.expense.domain.valueobject;

import io.github.sardul3.expense.domain.common.base.BaseId;

import java.util.UUID;

public class ExpenseGroupId extends BaseId<UUID> {
    private ExpenseGroupId(UUID uuid) {
        super(uuid);
    }

    public static ExpenseGroupId generate() {
        return new ExpenseGroupId(UUID.randomUUID());
    }
}
