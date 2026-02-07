package io.github.sardul3.expense.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateExpenseActivityRequest(
        @NotBlank(message = "cannot be empty")
        @Size(max = 50, message = "cannot be longer than 50 characters")
        String description,

        @NotNull(message = "cannot be null")
        @DecimalMin(value = "0.01", message = "must be positive")
        BigDecimal amount,

        @NotNull(message = "cannot be null")
        UUID paidBy,

        List<UUID> splitWith
) {
}
