package io.github.sardul3.account.domain;

import java.util.UUID;

public class ExpenseGroupId extends BaseId<UUID>{
    private ExpenseGroupId(UUID uuid) {
        super(uuid);
    }

    public static ExpenseGroupId generate() {
        return new ExpenseGroupId(UUID.randomUUID());
    }
}
