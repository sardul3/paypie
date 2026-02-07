package io.github.sardul3.expense.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Command to record a settlement between two participants in a group.
 *
 * @param fromParticipantId payer (participant who pays)
 * @param toParticipantId  receiver (participant who receives)
 * @param amount           amount to settle (positive)
 */
public record SettleUpCommand(UUID fromParticipantId, UUID toParticipantId, BigDecimal amount) {
}
