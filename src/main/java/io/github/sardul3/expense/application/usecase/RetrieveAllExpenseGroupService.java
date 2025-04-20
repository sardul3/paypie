package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.common.annotation.UseCase;
import io.github.sardul3.expense.application.dto.RetrieveExpenseGroupsResponse;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import io.github.sardul3.expense.application.port.in.RetrieveAllExpenseGroupsUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;

import java.util.List;
import java.util.Objects;

@UseCase(description = "Fetches all the expense groups in the system", inputPort = RetrieveAllExpenseGroupsUseCase.class)
public class RetrieveAllExpenseGroupService implements RetrieveAllExpenseGroupsUseCase {

    private final ExpenseGroupRepository expenseGroupRepository;

    public RetrieveAllExpenseGroupService(ExpenseGroupRepository expenseGroupRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
    }

    @Override
    public List<RetrieveExpenseGroupsResponse> getAllExpenseGroups() {
        return expenseGroupRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .filter(expenseGroup -> expenseGroup.getGroupName() != null)
                .map(entry -> new RetrieveExpenseGroupsResponse(entry.getGroupName().getName()))
                .toList();
    }
}
