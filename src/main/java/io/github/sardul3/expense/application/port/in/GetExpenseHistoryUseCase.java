package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.common.annotation.InputPort;
import io.github.sardul3.expense.application.dto.ExpenseHistoryPageResponse;

import java.util.UUID;

/**
 * Input port: returns paginated expense history for a group.
 */
@InputPort(description = "Returns paginated expense history for a group")
public interface GetExpenseHistoryUseCase {

    /**
     * Returns a page of expense activities for the group.
     *
     * @param groupId group id
     * @param page    zero-based page index
     * @param size    page size
     * @return paginated activity list
     * @throws io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException if group not found
     */
    ExpenseHistoryPageResponse getExpenseHistory(UUID groupId, int page, int size);
}
