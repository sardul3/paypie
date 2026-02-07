package io.github.sardul3.expense.adapter.out.persistence.postgres.repository;

import io.github.sardul3.expense.adapter.common.SecondaryAdapter;
import io.github.sardul3.expense.adapter.out.persistence.postgres.entity.ExpenseActivityEntity;
import io.github.sardul3.expense.application.dto.ActivityView;
import io.github.sardul3.expense.application.dto.ExpenseHistoryPageResponse;
import io.github.sardul3.expense.application.port.out.ExpenseActivityQueryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@SecondaryAdapter
@Component
public class PostgresExpenseActivityQueryRepository implements ExpenseActivityQueryRepository {

    private final ExpenseActivityJpaRepository expenseActivityJpaRepository;

    public PostgresExpenseActivityQueryRepository(ExpenseActivityJpaRepository expenseActivityJpaRepository) {
        this.expenseActivityJpaRepository = expenseActivityJpaRepository;
    }

    @Override
    public ExpenseHistoryPageResponse findByGroupId(UUID groupId, int page, int size) {
        var pageable = PageRequest.of(page, size);
        var springPage = expenseActivityJpaRepository.findByGroupId(groupId, pageable);
        var content = springPage.getContent().stream()
                .map(this::toActivityView)
                .toList();
        return new ExpenseHistoryPageResponse(
                content,
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.getNumber(),
                springPage.getSize()
        );
    }

    private ActivityView toActivityView(ExpenseActivityEntity e) {
        return new ActivityView(
                e.getId(),
                e.getDescription(),
                e.getAmount(),
                e.getPaidByParticipantId(),
                e.isSplitEvenly()
        );
    }
}
