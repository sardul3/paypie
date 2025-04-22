package io.github.sardul3;

import io.github.sardul3.expense.domain.model.ExpenseActivity;
import io.github.sardul3.expense.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertThrows;


class ExpenseActivityTest {

    @Test
    @DisplayName("Expense Activity | Expense should be created with name and amount")
    void expenseActivityShouldBeCreatedWithNameAndAmount() {
        ExpenseActivity activity = ExpenseActivity.from("grocery", Money.of(BigDecimal.TEN));
        assertThat(activity)
                .isNotNull()
                .hasFieldOrPropertyWithValue("description", "grocery")
                .hasFieldOrPropertyWithValue("amount", Money.of(BigDecimal.TEN));
    }

    @Test
    @DisplayName("ExpenseActivity.from() should throw IllegalArgumentException when money amount is zero")
    void shouldThrowExceptionWhenMoneyAmountIsZero() {
        // Arrange
        String category = "grocery";
        BigDecimal zeroAmount = BigDecimal.ZERO;

        // Act & Assert
        assertThatThrownBy(() -> ExpenseActivity.from(category, Money.of(zeroAmount)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expense amount must be positive and non-zero");
    }

    @Test
    @DisplayName("Expense Activity | Expense amount cannot less than $0")
    void expenseActivityShouldRejectNegativeMoneyAmount() {
        BigDecimal negative = BigDecimal.valueOf(-1);
        assertThrows(IllegalArgumentException.class,
                () -> ExpenseActivity.from("grocery", Money.of(negative)));
    }


}
