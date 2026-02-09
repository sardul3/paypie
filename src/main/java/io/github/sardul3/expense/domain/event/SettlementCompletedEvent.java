package io.github.sardul3.expense.domain.event;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Domain event raised when a settlement is recorded between two participants.
 *
 * @param groupId            expense group id
 * @param fromParticipantId  payer (participant who pays)
 * @param toParticipantId    receiver (participant who receives)
 * @param amount            amount settled
 */
public record SettlementCompletedEvent(UUID groupId, UUID fromParticipantId, UUID toParticipantId, BigDecimal amount)
        implements DomainEvent {
}
