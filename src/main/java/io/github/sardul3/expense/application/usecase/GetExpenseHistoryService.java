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

    /** Maximum allowed page size for expense history queries. */
    public static final int MAX_PAGE_SIZE = 500;

    private final ExpenseGroupRepository expenseGroupRepository;
    private final ExpenseActivityQueryRepository expenseActivityQueryRepository;

    public GetExpenseHistoryService(ExpenseGroupRepository expenseGroupRepository,
                                    ExpenseActivityQueryRepository expenseActivityQueryRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
        this.expenseActivityQueryRepository = expenseActivityQueryRepository;
    }

    @Override
    public ExpenseHistoryPageResponse getExpenseHistory(UUID groupId, int page, int size) {
        if (groupId == null) {
            throw new IllegalArgumentException("groupId cannot be null");
        }
        if (page < 0) {
            throw new IllegalArgumentException("page cannot be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive");
        }
        if (size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("size cannot exceed " + MAX_PAGE_SIZE);
        }

        expenseGroupRepository.findById(groupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException("Expense group not found: " + groupId));
        return expenseActivityQueryRepository.findByGroupId(groupId, page, size);
    }
}
