package io.github.sardul3.account.domain;

import java.math.BigDecimal;
import java.util.Objects;

@ValueObject(description = "Represents an amount in currency",
        boundedContext = "expense-management")

public class Money {
    private static final int MONEY_SCALE_DECIMAL_PLACES = 2;
    private BigDecimal amount;

    private Money(BigDecimal amount) {
        Objects.nonNull(amount);
        this.amount = amount.setScale(MONEY_SCALE_DECIMAL_PLACES, BigDecimal.ROUND_HALF_UP);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money withZeroBalance() {
        return new Money(BigDecimal.ZERO);
    }

    public Money add(Money other) {
        validateAmount(amount);
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        validateAmount(amount);
        return new Money(this.amount.subtract(other.amount));
    }

    public boolean isNegative() {
        return this.amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public void deduct(BigDecimal amount) {
        this.amount.subtract(amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    private void validateAmount(BigDecimal amount) {
        if(amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if(isNegative()) {
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
