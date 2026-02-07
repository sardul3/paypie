package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.common.annotation.UseCase;
import io.github.sardul3.expense.application.dto.GroupBalanceResponse;
import io.github.sardul3.expense.application.dto.ParticipantBalanceView;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.in.GetGroupBalanceUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;

import java.util.List;
import java.util.UUID;

/**
 * Use case: returns current balances for all participants in a group.
 */
@UseCase(description = "Returns group balance for all participants", inputPort = GetGroupBalanceUseCase.class)
public class GetGroupBalanceService implements GetGroupBalanceUseCase {

    private final ExpenseGroupRepository expenseGroupRepository;

    public GetGroupBalanceService(ExpenseGroupRepository expenseGroupRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
    }

    @Override
    public GroupBalanceResponse getBalance(UUID groupId) {
        ExpenseGroup group = expenseGroupRepository.findById(groupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException("Expense group not found: " + groupId));

        List<ParticipantBalanceView> participants = group.getParticipants().stream()
                .map(p -> new ParticipantBalanceView(
                        p.getParticipantId().getId(),
                        p.getEmail(),
                        p.getBalance()))
                .toList();

        return new GroupBalanceResponse(group.getId().getId(), participants);
    }
}
