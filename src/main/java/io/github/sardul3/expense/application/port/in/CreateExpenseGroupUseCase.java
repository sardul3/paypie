package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.common.annotation.InputPort;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;

@InputPort(description = "Handles command to create a new expense group via application layer")
public interface CreateExpenseGroupUseCase {
    CreateExpenseGroupResponse createExpenseGroup(CreateExpenseGroupCommand createExpenseGroupCommand);
}
