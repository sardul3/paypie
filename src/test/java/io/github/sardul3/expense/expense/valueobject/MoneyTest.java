package io.github.sardul3.expense.expense.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import io.github.sardul3.expense.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Money | Value Object Behavior")
class MoneyTest {

    @Test
    @DisplayName("Money | should be created with valid positive amount and scale to two decimal places")
    void shouldCreateMoneyWithValidAmount() {
        Money money = Money.of(new BigDecimal("10"));
        assertThat(money.getAmount()).isEqualByComparingTo("10.00");
    }

    @Test
    @DisplayName("Money | should reject null amount")
    void shouldNotAllowNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(null));
    }

    @Test
    @DisplayName("Money | should reject negative amount")
    void shouldNotAllowNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(new BigDecimal("-1")));
    }

    @Test
    @DisplayName("Money | should allow zero balance only through dedicated method")
    void shouldAllowZeroBalanceViaFactory() {
        Money zero = Money.withZeroBalance();
        assertThat(zero.getAmount()).isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("Money | should add two money objects")
    void shouldAddTwoMoneyInstances() {
        Money m1 = Money.of(new BigDecimal("10.00"));
        Money m2 = Money.of(new BigDecimal("5.50"));

        Money result = m1.add(m2);

        assertThat(result.getAmount()).isEqualByComparingTo("15.50");
    }

    @Test
    @DisplayName("Money | should subtract two money objects")
    void shouldSubtractTwoMoneyInstances() {
        Money m1 = Money.of(new BigDecimal("10.00"));
        Money m2 = Money.of(new BigDecimal("3.25"));

        Money result = m1.subtract(m2);

        assertThat(result.getAmount()).isEqualByComparingTo("6.75");
    }

    @Test
    @DisplayName("Money | should identify negative value correctly")
    void shouldDetectNegativeBalance() {
        Money m1 = Money.of(new BigDecimal("5.00"));
        Money m2 = Money.of(new BigDecimal("10.00"));

        Money result = m1.subtract(m2);

        assertTrue(result.isNegative());
    }

    @Test
    @DisplayName("Money | should identify not-positive balance")
    void shouldDetectNonPositiveMoney() {
        Money zero = Money.withZeroBalance();
        assertTrue(zero.isNotPositive());

        Money negative = Money.of(new BigDecimal("5.00")).subtract(Money.of(new BigDecimal("10.00")));
        assertTrue(negative.isNotPositive());
    }

    @Test
    @DisplayName("Money | equals and hashCode should match for same amount")
    void moneyEqualityShouldDependOnAmount() {
        Money m1 = Money.of(new BigDecimal("10.00"));
        Money m2 = Money.of(new BigDecimal("10"));

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    @DisplayName("Money | should be same when compared with same object")
    void moneyEqualityShouldBeTrueWhenComparedWithSameObject() {
        Money m1 = Money.of(new BigDecimal("10.00"));

        assertEquals(m1, m1);
        assertEquals(m1.hashCode(), m1.hashCode());
    }

    @Test
    @DisplayName("Money | should be different when compared with null")
    void moneyEqualityShouldBeTrueWhenComparedWithNullObject() {
        Money m1 = Money.of(new BigDecimal("10.00"));
        Money m2 = null;

        assertThat(m1).isNotEqualTo(m2);
    }

    @Test
    @DisplayName("Money | should be different when compared with a different type")
    void moneyEqualityShouldBeTrueWhenComparedWithDifferentType() {
        Money m1 = Money.of(new BigDecimal("10.00"));
        BigDecimal m2 = BigDecimal.valueOf(10L, 2);

        assertThat(m1).isNotEqualTo(m2);
    }

    @Test
    @DisplayName("Money | equals should not match for different amounts")
    void moneyWithDifferentAmountsShouldNotBeEqual() {
        Money m1 = Money.of(new BigDecimal("10.00"));
        Money m2 = Money.of(new BigDecimal("9.99"));

        assertNotEquals(m1, m2);
    }

    @Test
    @DisplayName("Money | toString should display plain amount")
    void toStringShouldBeFormattedCorrectly() {
        Money m1 = Money.of(new BigDecimal("123.456"));
        assertThat(m1.toString()).contains("123.46");
    }

    @Nested
    @DisplayName("Money | edge cases and precision")
    class MoneyPrecisionTests {

        @Test
        @DisplayName("should round correctly to two decimal places")
        void shouldRoundCorrectly() {
            Money rounded = Money.of(new BigDecimal("1.235"));
            assertThat(rounded.getAmount()).isEqualByComparingTo("1.24");

            Money roundedDown = Money.of(new BigDecimal("1.234"));
            assertThat(roundedDown.getAmount()).isEqualByComparingTo("1.23");
        }

        @Test
        @DisplayName("should handle high precision amounts safely")
        void shouldTrimScaleBeyondTwoDecimals() {
            Money highPrecision = Money.of(new BigDecimal("10.123456"));
            assertThat(highPrecision.getAmount().scale()).isEqualTo(2);
            assertThat(highPrecision.getAmount()).isEqualByComparingTo("10.12");
        }
    }
}
