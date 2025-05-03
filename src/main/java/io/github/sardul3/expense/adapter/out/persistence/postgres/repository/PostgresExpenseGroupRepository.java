package io.github.sardul3.expense.adapter.out.persistence.postgres.repository;

import io.github.sardul3.expense.adapter.common.SecondaryAdapter;
import io.github.sardul3.expense.adapter.out.persistence.postgres.entity.ExpenseGroupEntity;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SecondaryAdapter
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

    @Override
    public List<ExpenseGroup> findAll() {
        return expenseGroupJpaRepository.findAll()
                .stream()
                .map(entry -> {
                    return ExpenseGroup.from(GroupName.withName( entry.getName()), Participant.withEmail(entry.getCreatedBy()));
                })
                .toList();
    }

    @Override
    public Optional<ExpenseGroup> findById(UUID id) {
        return expenseGroupJpaRepository.findById(id)
                .map(expenseGroupEntity ->
                        ExpenseGroup.from(
                                GroupName.withName(expenseGroupEntity.getName()),
                                Participant.withEmail(expenseGroupEntity.getCreatedBy())
                        )
                );
    }
}
