package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.common.annotation.InputPort;
import io.github.sardul3.expense.application.dto.RetrieveExpenseGroupsResponse;

import java.util.List;

@InputPort(description = "Handles command to fetch all the expense groups present")
public interface RetrieveAllExpenseGroupsUseCase {
    List<RetrieveExpenseGroupsResponse> getAllExpenseGroups();
}
