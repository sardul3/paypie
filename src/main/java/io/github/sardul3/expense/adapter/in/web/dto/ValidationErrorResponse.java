package io.github.sardul3.expense.adapter.in.web.dto;

import java.time.Instant;
import java.util.List;

/**
 * Represents a structured validation error response containing multiple field-level errors.
 */
public record ValidationErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String path,
        List<FieldErrorResponse> errors
) {
    public static ValidationErrorResponse of(
            int status, String error, String path, List<FieldErrorResponse> errors
    ) {
        return new ValidationErrorResponse(Instant.now(), status, error, path, errors);
    }
}

