package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.common.annotation.UseCase;
import io.github.sardul3.expense.application.dto.ExpenseHistoryPageResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.in.GetExpenseHistoryUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseActivityQueryRepository;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;

import java.util.UUID;

/**
 * Use case: returns paginated expense history for a group.
 */
@UseCase(description = "Returns paginated expense history for a group", inputPort = GetExpenseHistoryUseCase.class)
public class GetExpenseHistoryService implements GetExpenseHistoryUseCase {

    private final ExpenseGroupRepository expenseGroupRepository;
    private final ExpenseActivityQueryRepository expenseActivityQueryRepository;

    public GetExpenseHistoryService(ExpenseGroupRepository expenseGroupRepository,
                                    ExpenseActivityQueryRepository expenseActivityQueryRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
        this.expenseActivityQueryRepository = expenseActivityQueryRepository;
    }

    @Override
    public ExpenseHistoryPageResponse getExpenseHistory(UUID groupId, int page, int size) {
        expenseGroupRepository.findById(groupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException("Expense group not found: " + groupId));
        return expenseActivityQueryRepository.findByGroupId(groupId, page, size);
    }
}
