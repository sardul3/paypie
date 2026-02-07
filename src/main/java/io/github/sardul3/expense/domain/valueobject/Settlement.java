package io.github.sardul3.expense.domain.valueobject;

import io.github.sardul3.expense.domain.common.annotation.ValueObject;

import java.util.Objects;

/**
 * Value object: a settlement between two participants (payer pays receiver an amount).
 */
@ValueObject(
        description = "Represents a settlement: one participant pays another to clear debt",
        boundedContext = "expense-management"
)
public final class Settlement {

    private final ParticipantId fromParticipantId; // payer (the one who pays)
    private final ParticipantId toParticipantId;   // receiver
    private final Money amount;

    private Settlement(ParticipantId fromParticipantId, ParticipantId toParticipantId, Money amount) {
        this.fromParticipantId = Objects.requireNonNull(fromParticipantId, "fromParticipantId cannot be null");
        this.toParticipantId = Objects.requireNonNull(toParticipantId, "toParticipantId cannot be null");
        this.amount = Objects.requireNonNull(amount, "amount cannot be null");
        if (fromParticipantId.equals(toParticipantId)) {
            throw new IllegalArgumentException("Settlement must be between two different participants");
        }
        if (amount.isNotPositive()) {
            throw new IllegalArgumentException("Settlement amount must be positive");
        }
    }

    /**
     * Creates a settlement: from pays to the given amount.
     *
     * @param fromParticipantId payer (participant who pays)
     * @param toParticipantId  receiver (participant who receives)
     * @param amount           amount to settle (must be positive)
     * @return new Settlement
     */
    public static Settlement of(ParticipantId fromParticipantId, ParticipantId toParticipantId, Money amount) {
        return new Settlement(fromParticipantId, toParticipantId, amount);
    }

    public ParticipantId getFromParticipantId() {
        return fromParticipantId;
    }

    public ParticipantId getToParticipantId() {
        return toParticipantId;
    }

    public Money getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settlement settlement = (Settlement) o;
        return Objects.equals(fromParticipantId, settlement.fromParticipantId)
                && Objects.equals(toParticipantId, settlement.toParticipantId)
                && Objects.equals(amount, settlement.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromParticipantId, toParticipantId, amount);
    }
}
