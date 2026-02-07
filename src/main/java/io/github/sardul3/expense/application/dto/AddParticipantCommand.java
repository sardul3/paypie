package io.github.sardul3.expense.application.dto;

/**
 * Command to add a participant to an expense group.
 *
 * @param email participant email address
 */
public record AddParticipantCommand(String email) {
}
