package io.github.sardul3.expense.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * View of an expense activity for history listing.
 *
 * @param id                   activity id
 * @param description          activity description
 * @param amount               amount
 * @param paidByParticipantId  participant who paid
 * @param splitEvenly          whether split was even for all members
 */
public record ActivityView(
        UUID id,
        String description,
        BigDecimal amount,
        UUID paidByParticipantId,
        boolean splitEvenly
) {
}
