package io.github.sardul3.expense.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request body for recording a settlement.
 *
 * @param fromParticipantId payer (participant who pays)
 * @param toParticipantId  receiver (participant who receives)
 * @param amount           amount to settle (must be positive)
 */
public record SettleUpRequest(
        @NotNull(message = "fromParticipantId is required")
        UUID fromParticipantId,

        @NotNull(message = "toParticipantId is required")
        UUID toParticipantId,

        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", message = "amount must be positive")
        BigDecimal amount
) {
}
