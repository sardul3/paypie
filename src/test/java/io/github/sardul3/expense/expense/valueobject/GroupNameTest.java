package io.github.sardul3.expense.expense.valueobject;

import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("GroupName | Value Object Behavior")
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
    @DisplayName("Expense Group | names cannot be more than allowed chars limit")
    void expenseGroupNameNameCannotBeMoreThanMaxLengthAllowedTest() {
        final int MAX_ALLOWED_CHAR_LIMIT = 50;
        String illegalGroupName = "t".repeat(MAX_ALLOWED_CHAR_LIMIT + 1);
        assertThrows(IllegalArgumentException.class, () -> GroupName.withName(illegalGroupName));
    }

    @Test
    @DisplayName("Expense Group | name of exactly 50 characters should be allowed")
    void groupNameExactlyAtLimitShouldBeAllowedTest() {
        String nameAtLimit = "a".repeat(50);
        GroupName groupName = GroupName.withName(nameAtLimit);
        assertEquals(nameAtLimit, groupName.getName());
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
        assertEquals(expenseGroupName.hashCode(), anotherExpenseGroupName.hashCode());
    }

    @Test
    @DisplayName("Expense Group | equality should be invalid if different types")
    void expenseGroupNameCheckEqualityTestWithDifferentTypes() {
        String groupName = " test-expense-group";
        String anotherGroupName = "test-expense-group  ";

        GroupName expenseGroupName = GroupName.withName(groupName);

        assertThat(expenseGroupName).isNotEqualTo(groupName);
    }

    @Test
    @DisplayName("Expense Group | equality should be invalid if compared with null")
    void expenseGroupNameCheckEqualityTestWithNullValue() {
        String groupName = " test-expense-group";
        String anotherGroupName = null;

        GroupName expenseGroupName = GroupName.withName(groupName);

        assertThat(expenseGroupName).isNotEqualTo(anotherGroupName);
    }


    @Test
    @DisplayName("Expense Group | toString() should include normalized name")
    void expenseGroupNameToStringShouldIncludeNormalizedNameTest() {
        String rawInput = "  test-expense-group  ";
        String expectedNormalized = rawInput.trim();

        GroupName groupName = GroupName.withName(rawInput);

        String toStringOutput = groupName.toString();

        assertThat(toStringOutput)
                .contains(expectedNormalized)
                .doesNotContain("  ") // optional: ensure raw spaces are not present
                .startsWith("GroupName{")
                .contains("name='" + expectedNormalized + "'");
    }

    @Test
    @DisplayName("Expense Group | getName() returns a copy-safe, immutable string")
    void expenseGroupNameShouldBeImmutableViaGetterTest() {
        String rawInput = "  test-expense-group  ";
        GroupName groupName = GroupName.withName(rawInput);

        String original = groupName.getName();
        String modified = original + "-hacked";

        // Ensure internal state is not affected
        assertThat(groupName.getName()).isEqualTo(original);
        assertThat(groupName.getName()).doesNotContain("hacked");
    }

}
