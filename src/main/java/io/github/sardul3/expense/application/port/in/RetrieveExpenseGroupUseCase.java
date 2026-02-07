package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.common.annotation.InputPort;
import io.github.sardul3.expense.application.dto.ExpenseGroupDetailResponse;

import java.util.UUID;

/**
 * Input port: retrieves a single expense group by id with participants and balances.
 */
@InputPort(description = "Retrieves expense group details by id including participants and balances")
public interface RetrieveExpenseGroupUseCase {

    /**
     * Returns the expense group with the given id.
     *
     * @param groupId the group id
     * @return group details including participants and balances
     * @throws io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException if not found
     */
    ExpenseGroupDetailResponse getExpenseGroup(UUID groupId);
}
