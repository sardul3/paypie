package io.github.sardul3.expense.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * View of a participant's identity and current balance within a group.
 */
public record ParticipantBalanceView(UUID participantId, String email, BigDecimal balance) {
}
