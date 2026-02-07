package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.common.annotation.InputPort;
import io.github.sardul3.expense.application.dto.SettleUpCommand;
import io.github.sardul3.expense.application.dto.SettleUpResponse;

import java.util.UUID;

/**
 * Input port: records a settlement (payer pays receiver) in a group.
 */
@InputPort(description = "Records a settlement between two participants in a group")
public interface SettleUpUseCase {

    /**
     * Records the settlement and updates participant balances.
     *
     * @param groupId the expense group id
     * @param command from/to participant ids and amount
     * @return response with group id
     * @throws io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException if group not found
     * @throws IllegalArgumentException if either participant not in group or amount invalid
     */
    SettleUpResponse settleUp(UUID groupId, SettleUpCommand command);
}
