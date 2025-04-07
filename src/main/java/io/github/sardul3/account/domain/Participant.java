package io.github.sardul3.account.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@DomainEntity(
        description = "Represents a participant in an expense group, with balance and email address",
        boundedContext = "expense-management",
        isAggregateRoot = false
)
public class Participant extends BaseEntity<ParticipantId> {
    private final ParticipantEmail participantEmail;

    private  Money balance;

    private Participant(ParticipantId id, ParticipantEmail participantEmail, Money balance) {
        super(id);
        this.participantEmail = participantEmail;
        this.balance = balance;
    }

    public static Participant withEmail(String email) {
        return new Participant(ParticipantId.generate(),
                ParticipantEmail.of(email),
                Money.withZeroBalance());
    }

    public void credit(Money creditMoney) {
        if (creditMoney.isNotPositive()) {
            throw new IllegalArgumentException("Cannot credit with zero or negative amount");
        }
        this.balance = this.balance.add(creditMoney);
    }

    public void debit(Money debitMoney) {
        if (debitMoney.isNotPositive()) {
            throw new IllegalArgumentException("Cannot debit with zero or negative amount");
        }
        this.balance = this.balance.subtract(debitMoney);
        if(balance.isNegative()) {
            throw new IllegalStateException("After debit, the money cannot be negative");
        }
    }

    public String getEmail() {
        return participantEmail.getEmail();
    }

    public UUID getId() {
        return id.getId();
    }

    public ParticipantId getParticipantId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance.getAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", participantEmail=" + participantEmail +
                ", balance=" + balance +
                '}';
    }
}
