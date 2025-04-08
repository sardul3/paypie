package io.github.sardul3.expense.adapter.in.web.dto;

/**
 * Represents an individual field validation error.
 */
public record FieldErrorResponse(String field, String message) {}
