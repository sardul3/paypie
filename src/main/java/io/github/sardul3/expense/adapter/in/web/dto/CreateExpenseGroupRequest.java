package io.github.sardul3.expense.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateExpenseGroupRequest(
        @NotBlank(message = "cannot be empty")
        String name,

        @NotBlank(message = "cannot be empty")
        @Email(message = "needs to be a valid email format")
        String createdBy
) {
}
