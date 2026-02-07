package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.common.annotation.InputPort;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityResponse;

/**
 * Input port: creates a new expense activity in an existing group. Splits the amount per participants.
 */
@InputPort(description = "Handles command to create a new expense activity within a group")
public interface CreateExpenseActivityUseCase {
    /**
     * Adds an expense activity to the group and updates participant balances.
     *
     * @param command group id, description, amount, payer, optional split participants
     * @return response with description, amount, and updated payer balance
     */
    CreateExpenseActivityResponse createExpenseActivity(CreateExpenseActivityCommand command);
}
