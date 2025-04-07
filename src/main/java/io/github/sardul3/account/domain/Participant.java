package io.github.sardul3.account.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Participant {
    private final UUID id;
    private final ParticipantEmail participantEmail;

    private  Money balance;

    private Participant(UUID id, ParticipantEmail participantEmail, Money balance) {
        this.id = id;
        this.participantEmail = participantEmail;
        this.balance = balance;
    }

    public static Participant withEmail(String email) {
        return new Participant(UUID.randomUUID(),
                ParticipantEmail.of(email),
                Money.withZeroBalance());
    }

    public void credit(Money creditMoney) {
        this.balance = this.balance.add(creditMoney);
    }

    public void debit(Money debitMoney) {
        this.balance = this.balance.subtract(debitMoney);
    }

    public String getEmail() {
        return participantEmail.getEmail();
    }

    public UUID getId() {
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


}
