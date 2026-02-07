package io.github.sardul3.expense.application.dto;

import java.util.List;
import java.util.UUID;

/**
 * Full detail of an expense group including participants and their balances.
 */
public record ExpenseGroupDetailResponse(
        UUID id,
        String name,
        String createdBy,
        boolean activated,
        List<ParticipantBalanceView> participants
) {
}
