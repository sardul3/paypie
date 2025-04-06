package io.github.sardul3;

import io.github.sardul3.account.domain.ExpenseGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpenseGroupTest {

    @Test
    @DisplayName("Expense Group | should be created without errors")
    void expenseGroupShouldBeCreatedTest() {
        final String GROUP_NAME = "test-expense-group";
        ExpenseGroup expenseGroup = ExpenseGroup.withName(GROUP_NAME);
        assertEquals(GROUP_NAME, expenseGroup.getName());
    }

    @ParameterizedTest
    @DisplayName("Expense Group | should reject invalid empty names")
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    void expenseGroupShouldNotBeEmptyTest() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> ExpenseGroup.withName(""));

        assertThat(exception.getMessage())
                .isNotEmpty()
                .contains("Name cannot");
    }

    @Test
    @DisplayName("Expense Group | should reject names passed as null")
    void expenseGroupShouldNotBeNullTest() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> ExpenseGroup.withName(null));

        assertThat(exception.getMessage())
                .isNotEmpty()
                .contains("Name cannot");
    }

}
