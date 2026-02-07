package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.common.annotation.InputPort;
import io.github.sardul3.expense.application.dto.RetrieveExpenseGroupsResponse;

import java.util.List;

/**
 * Input port: retrieves all expense groups (e.g. for listing).
 */
@InputPort(description = "Handles command to fetch all the expense groups present")
public interface RetrieveAllExpenseGroupsUseCase {
    /** Returns all expense groups in the system. */
    List<RetrieveExpenseGroupsResponse> getAllExpenseGroups();
}
