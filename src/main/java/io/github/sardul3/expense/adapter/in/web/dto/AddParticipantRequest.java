package io.github.sardul3.expense.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request body for adding a participant to an expense group.
 *
 * @param email participant email address
 */
public record AddParticipantRequest(
        @NotBlank(message = "cannot be empty")
        @Email(message = "must be a valid email format")
        String email
) {
}
