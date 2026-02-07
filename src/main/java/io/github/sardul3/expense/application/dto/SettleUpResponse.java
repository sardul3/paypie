package io.github.sardul3.expense.application.dto;

import java.util.UUID;

/**
 * Response after recording a settlement.
 *
 * @param groupId group where settlement was recorded
 */
public record SettleUpResponse(UUID groupId) {
}
