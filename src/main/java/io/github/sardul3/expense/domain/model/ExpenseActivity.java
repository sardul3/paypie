package io.github.sardul3.expense.domain.model;

import io.github.sardul3.expense.domain.valueobject.Money;

public class ExpenseActivity {
    private final String description;
    private final Money amount;

    private ExpenseActivity(String description, Money amount) {
        this.description = description;
        this.amount = amount;
    }

    public static ExpenseActivity from(String description, Money amount) {
        if(amount.isNotPositive()) {
           throw new IllegalArgumentException("Expense amount must be positive and non-zero");
        }
        return new ExpenseActivity(description, amount);
    }



}
