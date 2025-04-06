package io.github.sardul3;

import io.github.sardul3.account.domain.GroupName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GroupNameTest {

    @Test
    @DisplayName("Expense Group | should be created without errors")
    void expenseGroupNameShouldBeCreatedTest() {
        final String GROUP_NAME = "test-expense-group";
        GroupName expenseGroupName = GroupName.withName(GROUP_NAME);
        assertEquals(GROUP_NAME, expenseGroupName.getName());
    }

    @ParameterizedTest
    @DisplayName("Expense Group | should reject invalid empty names")
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void expenseGroupNameShouldNotBeEmptyTest(String invalidName) {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> GroupName.withName(invalidName));

        assertThat(exception.getMessage())
                .isNotEmpty()
                .contains("Name cannot");
    }

    @Test
    @DisplayName("Expense Group | should reject names passed as null")
    void expenseGroupNameShouldNotBeNullTest() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> GroupName.withName(null));

        assertThat(exception.getMessage())
                .isNotEmpty()
                .contains("Name cannot");
    }

    @Test
    @DisplayName("Expense Group | names cannot be more than 50 chars")
    void expenseGroupNameNameCannotBeMoreThanMaxLengthAllowedTest() {
        String illegalGroupName = "test-expense-group-random-falcon-with-wings-and-very-good-flight";
        assertThrows(IllegalArgumentException.class, () -> GroupName.withName(illegalGroupName));
    }

    @Test
    @DisplayName("Expense Group | names cannot have trailing spaces")
    void expenseGroupNameNameShouldNotContainTrailingSpacesTest() {
        String groupName = "test-expense-group ";
        String groupNameWithoutSpaces = groupName.substring(0, groupName.length() - 1);

        GroupName expenseGroupName = GroupName.withName(groupName);
        assertEquals(groupNameWithoutSpaces, expenseGroupName.getName());
        assertEquals(groupNameWithoutSpaces.length(), expenseGroupName.getName().length());
    }

    @Test
    @DisplayName("Expense Group | names cannot have leading spaces")
    void expenseGroupNameNameShouldNotContainLeadingSpacesTest() {
        String groupName = " test-expense-group";
        String groupNameWithoutSpaces = groupName.substring(1, groupName.length());

        GroupName expenseGroupName = GroupName.withName(groupName);
        assertEquals(groupNameWithoutSpaces, expenseGroupName.getName());
        assertEquals(groupNameWithoutSpaces.length(), expenseGroupName.getName().length());
    }

    @Test
    @DisplayName("Expense Group | names cannot have spaces on either end")
    void expenseGroupNameNameShouldNotContainSpacesOnEitherEndTest() {
        String groupName = " test-expense-group   ";
        String groupNameWithoutSpaces = groupName.trim();

        GroupName expenseGroupName = GroupName.withName(groupName);
        assertEquals(groupNameWithoutSpaces, expenseGroupName.getName());
        assertEquals(groupNameWithoutSpaces.length(), expenseGroupName.getName().length());
    }

    @Test
    @DisplayName("Expense Group | equality should be based on value")
    void expenseGroupNameCheckEqualityTest() {
        String groupName = " test-expense-group";
        String anotherGroupName = "test-expense-group  ";

        GroupName expenseGroupName = GroupName.withName(groupName);
        GroupName anotherExpenseGroupName = GroupName.withName(anotherGroupName);

        assertEquals(expenseGroupName, anotherExpenseGroupName);
    }
}
