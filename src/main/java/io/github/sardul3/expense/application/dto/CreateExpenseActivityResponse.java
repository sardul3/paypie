package io.github.sardul3.expense.application.dto;

import java.math.BigDecimal;

public record CreateExpenseActivityResponse(
        String description,
        String amount,
        BigDecimal payerBalance
) {
}
