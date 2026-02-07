package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.common.annotation.InputPort;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;

/**
 * Input port: creates a new expense group. Validates name uniqueness and persists the aggregate.
 */
@InputPort(description = "Handles command to create a new expense group via application layer")
public interface CreateExpenseGroupUseCase {
    /**
     * Creates an expense group with the given name and creator.
     *
     * @param createExpenseGroupCommand name and creator email
     * @return response with id and name
     */
    CreateExpenseGroupResponse createExpenseGroup(CreateExpenseGroupCommand createExpenseGroupCommand);
}
