package io.github.sardul3.expense.application.dto;

import java.util.UUID;

/**
 * Response after adding a participant to a group.
 *
 * @param participantId id of the added participant
 * @param email         participant email
 */
public record AddParticipantResponse(UUID participantId, String email) {
}
