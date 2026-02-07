package io.github.sardul3.expense.application.dto;

import java.util.List;
import java.util.UUID;

/**
 * Balance view for a group: group id and participant balances.
 */
public record GroupBalanceResponse(UUID groupId, List<ParticipantBalanceView> participants) {
}
