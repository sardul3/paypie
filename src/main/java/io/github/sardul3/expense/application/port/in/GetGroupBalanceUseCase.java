package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.common.annotation.InputPort;
import io.github.sardul3.expense.application.dto.GroupBalanceResponse;

import java.util.UUID;

/**
 * Input port: returns current balances for all participants in a group.
 */
@InputPort(description = "Returns group balance view for all participants")
public interface GetGroupBalanceUseCase {

    /**
     * Returns balance view for the group.
     *
     * @param groupId the expense group id
     * @return group id and participant balances
     * @throws io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException if group not found
     */
    GroupBalanceResponse getBalance(UUID groupId);
}
