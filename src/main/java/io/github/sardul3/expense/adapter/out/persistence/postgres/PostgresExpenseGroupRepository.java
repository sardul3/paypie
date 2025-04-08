package io.github.sardul3.expense.adapter.out.persistence.postgres;

import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.springframework.stereotype.Component;

@Component
public class PostgresExpenseGroupRepository implements ExpenseGroupRepository {

    private final ExpenseGroupJpaRepository expenseGroupJpaRepository;

    public PostgresExpenseGroupRepository(ExpenseGroupJpaRepository expenseGroupJpaRepository) {
        this.expenseGroupJpaRepository = expenseGroupJpaRepository;
    }

    @Override
    public boolean existsByName(GroupName groupName) {
        return expenseGroupJpaRepository.existsByName(groupName.getName());
    }

    @Override
    public ExpenseGroup save(ExpenseGroup expenseGroup) {
        ExpenseGroupEntity expenseGroupEntity =
                ExpenseGroupEntity.builder()
                        .id(expenseGroup.getId().getId())
                        .activated(false)
                        .name(expenseGroup.getGroupName().getName())
                        .createdBy(expenseGroup.getGroupCreator().getEmail())
                        .build();
        expenseGroupJpaRepository.save(expenseGroupEntity);
        return expenseGroup;
    }
}
