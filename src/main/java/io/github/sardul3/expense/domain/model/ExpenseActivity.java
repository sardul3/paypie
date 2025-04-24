package io.github.sardul3.expense.domain.model;

import io.github.sardul3.expense.domain.common.annotation.DomainEntity;
import io.github.sardul3.expense.domain.valueobject.ExpenseSplit;
import io.github.sardul3.expense.domain.valueobject.Money;
import io.github.sardul3.expense.domain.valueobject.ParticipantId;

import java.util.List;

@DomainEntity(
        description = "Represents an activity in the expense group, with description and amount",
        boundedContext = "expense-management",
        isAggregateRoot = false
)
public class ExpenseActivity {
    private static final int MAX_DESCRIPTION_LENGTH = 50;

    private final String description;
    private final Money amount;
    private final Participant paidBy;

    private ExpenseSplit split;

    private ExpenseActivity(String description, Money amount, Participant paidBy) {
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.split = new ExpenseSplit(true);
    }

    private ExpenseActivity(String description, Money amount, Participant paidBy, List<ParticipantId> splitMembers) {
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.split = new ExpenseSplit(false);
        this.split.setSplitMembers(splitMembers);
    }

    public static ExpenseActivity from(String description, Money amount, Participant paidBy) {
        validateExpenseAmount(amount);
        validateExpenseDescription(description);
        return new ExpenseActivity(description, amount, paidBy);
    }

    private static void validateExpenseAmount(Money amount) {
        if(amount.isNotPositive()) {
           throw new IllegalArgumentException("Expense amount must be positive and non-zero");
        }
    }

    private static void validateExpenseDescription(String description) {
        if(description == null) {
            throw new IllegalArgumentException("Expense description cannot be null");
        }
        if(description.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense description cannot be empty");
        }
        if(description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Expense description cannot be longer than 50 characters");
        }
    }

    public static ExpenseActivity from(String description, Money amount, Participant paidBy, List<ParticipantId> splitMembers) {
        validateExpenseAmount(amount);
        validateExpenseDescription(description);
        return new ExpenseActivity(description, amount, paidBy, splitMembers);
    }

    public Participant getPaidBy() {
        return paidBy;
    }

    public Money getAmount() {
        return amount;
    }

    public ExpenseSplit getSplit() {
        return split;
    }

    public void setSplit(ExpenseSplit split) {
        this.split = split;
    }
}
