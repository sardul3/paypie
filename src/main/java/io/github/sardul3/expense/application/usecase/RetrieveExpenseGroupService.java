package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.common.annotation.UseCase;
import io.github.sardul3.expense.application.dto.ExpenseGroupDetailResponse;
import io.github.sardul3.expense.application.dto.ParticipantBalanceView;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.in.RetrieveExpenseGroupUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;

import java.util.List;
import java.util.UUID;

/**
 * Use case: fetches a single expense group by id and maps to detail response with participants and balances.
 */
@UseCase(description = "Retrieves expense group by id with participants and balances", inputPort = RetrieveExpenseGroupUseCase.class)
public class RetrieveExpenseGroupService implements RetrieveExpenseGroupUseCase {

    private final ExpenseGroupRepository expenseGroupRepository;

    public RetrieveExpenseGroupService(ExpenseGroupRepository expenseGroupRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
    }

    @Override
    public ExpenseGroupDetailResponse getExpenseGroup(UUID groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("groupId cannot be null");
        }
        ExpenseGroup group = expenseGroupRepository.findById(groupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException("Expense group not found: " + groupId));

        List<ParticipantBalanceView> participants = group.getParticipants().stream()
                .map(this::toParticipantBalanceView)
                .toList();

        return new ExpenseGroupDetailResponse(
                group.getId().getId(),
                group.getGroupName().getName(),
                group.getGroupCreator().getEmail(),
                group.isActivated(),
                participants
        );
    }

    private ParticipantBalanceView toParticipantBalanceView(Participant p) {
        return new ParticipantBalanceView(
                p.getParticipantId().getId(),
                p.getEmail(),
                p.getBalance()
        );
    }
}
