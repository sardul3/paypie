package io.github.sardul3.expense.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateExpenseActivityCommand(
        UUID groupId,
        String description,
        BigDecimal amount,
        UUID paidBy,
        List<UUID> splitWith
) {
}
