package io.github.sardul3.expense.domain.event;

import java.util.UUID;

/**
 * Domain event raised when a participant is added to an expense group.
 *
 * @param groupId       expense group id
 * @param participantId participant id
 * @param email         participant email
 */
public record ParticipantAddedEvent(UUID groupId, UUID participantId, String email) implements DomainEvent {
}
