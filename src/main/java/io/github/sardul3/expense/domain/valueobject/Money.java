package io.github.sardul3.expense.domain.valueobject;

import io.github.sardul3.expense.domain.common.annotation.ValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value object: immutable monetary amount with scale 2 and HALF_UP rounding.
 */
@ValueObject(description = "Represents an amount in currency",
        boundedContext = "expense-management")
public class Money {
    private static final int MONEY_SCALE_DECIMAL_PLACES = 2;

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null")
                .setScale(MONEY_SCALE_DECIMAL_PLACES, RoundingMode.HALF_UP);
    }

    public static Money of(BigDecimal amount) {
        validateAmount(amount);
        return new Money(amount);
    }

    public static Money withZeroBalance() {
        return new Money(BigDecimal.ZERO);
    }

    /**
     * Creates Money from a balance amount (e.g. when reconstituting from persistence).
     * Allows negative values for balance representation.
     *
     * @param amount balance amount (may be negative)
     * @return Money with given amount
     */
    public static Money fromBalance(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        return new Money(amount.setScale(MONEY_SCALE_DECIMAL_PLACES, RoundingMode.HALF_UP));
    }

    public Money add(Money other) {
        if(other.isNegative()) {
            throw new IllegalArgumentException("Cannot add negative money");
        }
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        BigDecimal result = this.amount.subtract(other.amount);
        return new Money(result);
    }

    public Money split(int ways) {
        validateAmount(amount);
        if (ways <= 0) throw new IllegalArgumentException("Divisor must be greater than zero");
        return new Money(this.amount.divide(BigDecimal.valueOf(ways),
                MONEY_SCALE_DECIMAL_PLACES, RoundingMode.HALF_UP));
    }

    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isNotPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) <= 0;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    private static void validateAmount(BigDecimal amount) {
        if(amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount.toPlainString() +
                '}';
    }
}
