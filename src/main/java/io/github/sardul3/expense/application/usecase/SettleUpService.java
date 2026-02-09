package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.common.annotation.UseCase;
import io.github.sardul3.expense.application.dto.SettleUpCommand;
import io.github.sardul3.expense.application.dto.SettleUpResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.in.SettleUpUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.valueobject.Money;
import io.github.sardul3.expense.domain.valueobject.ParticipantId;
import io.github.sardul3.expense.domain.valueobject.Settlement;

import java.util.Objects;
import java.util.UUID;

/**
 * Use case: records a settlement between two participants and persists the group.
 */
@UseCase(description = "Records a settlement and updates balances", inputPort = SettleUpUseCase.class)
public class SettleUpService implements SettleUpUseCase {

    private final ExpenseGroupRepository expenseGroupRepository;

    public SettleUpService(ExpenseGroupRepository expenseGroupRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
    }

    @Override
    public SettleUpResponse settleUp(UUID groupId, SettleUpCommand command) {
        if (groupId == null) {
            throw new IllegalArgumentException("groupId cannot be null");
        }
        Objects.requireNonNull(command, "SettleUpCommand cannot be null");

        ExpenseGroup group = expenseGroupRepository.findById(groupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException("Expense group not found: " + groupId));

        Settlement settlement = Settlement.of(
                ParticipantId.from(command.fromParticipantId()),
                ParticipantId.from(command.toParticipantId()),
                Money.of(command.amount())
        );
        group.settle(settlement);
        expenseGroupRepository.save(group);

        return new SettleUpResponse(groupId);
    }
}
