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
        if(description == null) {
            throw new IllegalArgumentException("Expense description cannot be null");
        }
        if(description.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense description cannot be empty");
        }
        if(description.length() > 50) {
            throw new IllegalArgumentException("Expense description cannot be longer than 50 characters");
        }
        return new ExpenseActivity(description, amount);
    }



}
