package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.dto.CreateExpenseGroupCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;

public interface CreateExpenseGroupUseCase {
    CreateExpenseGroupResponse createExpenseGroup(CreateExpenseGroupCommand CreateExpenseGroupCommand);
}
