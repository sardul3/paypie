package io.github.sardul3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpenseGroupTest {

    @Test
    void expenseGroupShouldBeCreated() {
        final String GROUP_NAME = "test-expense-group";
        ExpenseGroup expenseGroup = ExpenseGroup.withName(GROUP_NAME);
        assertEquals(GROUP_NAME, expenseGroup.getName());
    }
}
