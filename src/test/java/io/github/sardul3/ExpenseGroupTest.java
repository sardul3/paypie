package io.github.sardul3;

import io.github.sardul3.account.domain.ExpenseGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpenseGroupTest {

    @Test
    @DisplayName("Expense Group | should be created without errors")
    void expenseGroupShouldBeCreatedTest() {
        final String GROUP_NAME = "test-expense-group";
        ExpenseGroup expenseGroup = ExpenseGroup.withName(GROUP_NAME);
        assertEquals(GROUP_NAME, expenseGroup.getName());
    }

    @Test
    @DisplayName("Expense Group | should not be created for empty name")
    void expenseGroupShouldNotBeEmptyTest() {
        assertThrows(IllegalArgumentException.class, () -> ExpenseGroup.withName(""));
    }

}
