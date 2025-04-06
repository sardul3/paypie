package io.github.sardul3.account.domain;

public class ExpenseGroup {
    private final String name;

    public ExpenseGroup(String name) {
        this.name = name;
    }

    public static ExpenseGroup withName(String name) {
        return new ExpenseGroup(name);
    }

    public String getName() {
        return name;
    }
}
