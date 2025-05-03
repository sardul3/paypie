package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.common.annotation.InputPort;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityResponse;

@InputPort(description = "Handles command to create a new expense activity within a group")
public interface CreateExpenseActivityUseCase {
    CreateExpenseActivityResponse createExpenseActivity(CreateExpenseActivityCommand command);
}
