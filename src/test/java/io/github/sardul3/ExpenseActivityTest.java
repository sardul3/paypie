package io.github.sardul3;

import io.github.sardul3.expense.domain.model.ExpenseActivity;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.ExpenseSplit;
import io.github.sardul3.expense.domain.valueobject.Money;
import io.github.sardul3.expense.domain.valueobject.ParticipantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertThrows;

class ExpenseActivityTest {

    @Test
    @DisplayName("Expense Activity | Expense should be created with name and amount")
    void expenseActivityShouldBeCreatedWithNameAndAmount() {
        Participant paidBy = Participant.withEmail("user@group1.com");
        ExpenseActivity activity = ExpenseActivity.from("grocery", Money.of(BigDecimal.TEN), Participant.withEmail("user@group2.com"));
        assertThat(activity)
                .isNotNull()
                .hasFieldOrPropertyWithValue("description", "grocery")
                .hasFieldOrPropertyWithValue("amount", Money.of(BigDecimal.TEN));
    }

    @Test
    @DisplayName("Expense Activity | Expense should be created with default split strategy")
    void expenseActivityShouldBeCreatedWithDefaultSplitStrategy() {
        Participant paidBy = Participant.withEmail("user@group1.com");
        ExpenseActivity activity = ExpenseActivity.from("grocery", Money.of(BigDecimal.TEN), paidBy);
        assertThat(activity)
                .isNotNull()
                .hasFieldOrPropertyWithValue("description", "grocery")
                .hasFieldOrPropertyWithValue("amount", Money.of(BigDecimal.TEN));

        assertThat(activity)
                .extracting(ExpenseActivity::getSplit)
                .extracting(ExpenseSplit::isSplitEvenlyForAllMembers)
                .isEqualTo(true);
    }

    @Test
    void expenseActivityShouldEnableSettingDifferentSplitStrategy() {
        Participant paidBy = Participant.withEmail("user@group1.com");
        Participant groupMember = Participant.withEmail("user1@group1.com");
        ExpenseActivity activity = ExpenseActivity.from("grocery", Money.of(BigDecimal.TEN), paidBy);

        ExpenseSplit split = new ExpenseSplit(false);
        split.setSplitMembers(List.of(paidBy.getParticipantId(), groupMember.getParticipantId()));
        activity.setSplit(split);

        assertThat(activity)
                .extracting(ExpenseActivity::getSplit)
                .extracting(ExpenseSplit::isSplitEvenlyForAllMembers)
                .isEqualTo(false);
    }

    @Test
    @DisplayName("ExpenseActivity.from() should throw IllegalArgumentException when money amount is zero")
    void shouldThrowExceptionWhenMoneyAmountIsZero() {
        // Arrange
        String category = "grocery";
        BigDecimal zeroAmount = BigDecimal.ZERO;

        // Act & Assert
        assertThatThrownBy(() -> ExpenseActivity.from(category, Money.of(zeroAmount), Participant.withEmail("user@group1.com")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expense amount must be positive and non-zero");
    }

    @Test
    @DisplayName("Expense Activity | Expense amount cannot less than $0")
    void expenseActivityShouldRejectNegativeMoneyAmount() {
        BigDecimal negative = BigDecimal.valueOf(-1);
        assertThrows(IllegalArgumentException.class,
                () -> ExpenseActivity.from("grocery", Money.of(negative), Participant.withEmail("user@group1.com")));
    }

    @Test
    @DisplayName("Expense Activity | Expense description cannot be empty")
    void expenseActivityShouldRejectEmptyDescription() {
        String category = "";
        Money money = Money.of(BigDecimal.TEN);
        assertThatThrownBy(() -> ExpenseActivity.from(category, money, Participant.withEmail("user@group1.com")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("description cannot be empty");
    }

    @Test
    @DisplayName("Expense Activity | Expense description cannot be null")
    void expenseActivityShouldRejectNullDescription() {
        String category = null;
        Money money = Money.of(BigDecimal.TEN);
        assertThatThrownBy(() -> ExpenseActivity.from(category, money, Participant.withEmail("user@group1.com")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("description cannot be null");
    }

    @Test
    @DisplayName("Expense Activity | Expense description cannot be more than 50 characters long")
    void expenseActivityShouldRejectAVeryLongDescription() {
        final int maxValidDescriptionLength = 50;
        String category = "g".repeat(maxValidDescriptionLength + 1);
        Money money = Money.of(BigDecimal.TEN);
        assertThatThrownBy(() -> ExpenseActivity.from(category, money, Participant.withEmail("user@group1.com")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("description cannot be longer than 50 characters");
    }

    @Test
    @DisplayName("Expense Activity | Expense description validation should pass when exactly 50 characters long")
    void expenseActivityShouldAcceptBorderLineLegalDescription() {
        final int maxValidDescriptionLength = 50;
        String category = "g".repeat(maxValidDescriptionLength);
        Money money = Money.of(BigDecimal.TEN);
        Participant paidBy = Participant.withEmail("user@group1.com");

        ExpenseActivity activity = ExpenseActivity.from(category, money, paidBy);

        assertThat(activity)
                .isNotNull()
                .hasFieldOrPropertyWithValue("description", category)
                .hasFieldOrPropertyWithValue("amount", Money.of(BigDecimal.TEN))
                .extracting(ExpenseActivity::getPaidBy)
                .extracting(Participant::getParticipantId)
                .isEqualTo(paidBy.getParticipantId());
    }

    @Test
    @DisplayName("Expense Activity | Expense description that is valid should create new instance of expense-activity")
    void expenseActivityShouldAcceptAValidExpenseDescription() {
        final int maxValidDescriptionLength = 50;
        String category = "g".repeat(maxValidDescriptionLength - 10);
        Money money = Money.of(BigDecimal.TEN);
        Participant paidBy = Participant.withEmail("user@group1.com");

        ExpenseActivity activity = ExpenseActivity.from(category, money, paidBy);

        assertThat(activity)
                .isNotNull()
                .hasFieldOrPropertyWithValue("description", category)
                .hasFieldOrPropertyWithValue("amount", Money.of(BigDecimal.TEN))
                .extracting(ExpenseActivity::getPaidBy)
                .extracting(Participant::getParticipantId)
                .isEqualTo(paidBy.getParticipantId());

    }



}
