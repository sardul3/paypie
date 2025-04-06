package io.github.sardul3;

import io.github.sardul3.account.domain.ExpenseGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperties;
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

    @Test
    @DisplayName("Expense Group | names cannot be more than 50 chars")
    void expenseGroupNameCannotBeMoreThanMaxLengthAllowedTest() {
        String illegalGroupName = "test-expense-group-random-falcon-with-wings-and-very-good-flight";
        assertThrows(IllegalArgumentException.class, () -> ExpenseGroup.withName(illegalGroupName));
    }

    @Test
    @DisplayName("Expense Group | names cannot have trailing spaces")
    void expenseGroupNameShouldNotContainTrailingSpacesTest() {
        String groupName = "test-expense-group ";
        String groupNameWithoutSpaces = groupName.substring(0, groupName.length() - 1);

        ExpenseGroup expenseGroup = ExpenseGroup.withName(groupName);
        assertEquals(groupNameWithoutSpaces, expenseGroup.getName());
        assertEquals(groupNameWithoutSpaces.length(), expenseGroup.getName().length());
    }

    @Test
    @DisplayName("Expense Group | names cannot have leading spaces")
    void expenseGroupNameShouldNotContainLeadingSpacesTest() {
        String groupName = " test-expense-group";
        String groupNameWithoutSpaces = groupName.substring(1, groupName.length());

        ExpenseGroup expenseGroup = ExpenseGroup.withName(groupName);
        assertEquals(groupNameWithoutSpaces, expenseGroup.getName());
        assertEquals(groupNameWithoutSpaces.length(), expenseGroup.getName().length());
    }

    @Test
    @DisplayName("Expense Group | names cannot have spaces on either end")
    void expenseGroupNameShouldNotContainSpacesOnEitherEndTest() {
        String groupName = " test-expense-group   ";
        String groupNameWithoutSpaces = groupName.trim();

        ExpenseGroup expenseGroup = ExpenseGroup.withName(groupName);
        assertEquals(groupNameWithoutSpaces, expenseGroup.getName());
        assertEquals(groupNameWithoutSpaces.length(), expenseGroup.getName().length());
    }

}
