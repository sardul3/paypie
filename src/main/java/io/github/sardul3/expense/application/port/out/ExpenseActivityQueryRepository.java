package io.github.sardul3.expense.application.port.out;

import io.github.sardul3.expense.application.common.annotation.OutputPort;
import io.github.sardul3.expense.application.dto.ActivityView;
import io.github.sardul3.expense.application.dto.ExpenseHistoryPageResponse;

import java.util.UUID;

/**
 * Output port: query expense activities for a group with pagination.
 */
@OutputPort(description = "Query expense activities for a group with pagination", role = OutputPort.Role.PERSISTENCE)
public interface ExpenseActivityQueryRepository {

    /**
     * Returns a page of activities for the group.
     *
     * @param groupId group id
     * @param page    zero-based page index
     * @param size    page size
     * @return paginated response
     */
    ExpenseHistoryPageResponse findByGroupId(UUID groupId, int page, int size);
}
