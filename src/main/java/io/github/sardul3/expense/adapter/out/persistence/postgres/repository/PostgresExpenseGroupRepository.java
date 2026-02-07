package io.github.sardul3.expense.adapter.out.persistence.postgres.repository;

import io.github.sardul3.expense.adapter.common.SecondaryAdapter;
import io.github.sardul3.expense.adapter.out.persistence.postgres.entity.ExpenseGroupEntity;
import io.github.sardul3.expense.adapter.out.persistence.postgres.entity.ParticipantEntity;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.ExpenseGroupId;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import io.github.sardul3.expense.domain.valueobject.Money;
import io.github.sardul3.expense.domain.valueobject.ParticipantId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SecondaryAdapter
@Component
public class PostgresExpenseGroupRepository implements ExpenseGroupRepository {

    private final ExpenseGroupJpaRepository expenseGroupJpaRepository;
    private final ParticipantJpaRepository participantJpaRepository;

    public PostgresExpenseGroupRepository(ExpenseGroupJpaRepository expenseGroupJpaRepository,
                                          ParticipantJpaRepository participantJpaRepository) {
        this.expenseGroupJpaRepository = expenseGroupJpaRepository;
        this.participantJpaRepository = participantJpaRepository;
    }

    @Override
    public boolean existsByName(GroupName groupName) {
        return expenseGroupJpaRepository.existsByName(groupName.getName());
    }

    @Override
    @Transactional
    public ExpenseGroup save(ExpenseGroup expenseGroup) {
        ExpenseGroupEntity expenseGroupEntity =
                ExpenseGroupEntity.builder()
                        .id(expenseGroup.getId().getId())
                        .activated(expenseGroup.isActivated())
                        .name(expenseGroup.getGroupName().getName())
                        .createdBy(expenseGroup.getGroupCreator().getEmail())
                        .build();
        expenseGroupJpaRepository.save(expenseGroupEntity);

        participantJpaRepository.deleteByGroupId(expenseGroup.getId().getId());
        for (Participant p : expenseGroup.getParticipants()) {
            ParticipantEntity pe = new ParticipantEntity(
                    expenseGroup.getId().getId(),
                    p.getParticipantId().getId(),
                    p.getEmail(),
                    p.getBalance()
            );
            participantJpaRepository.save(pe);
        }
        return expenseGroup;
    }

    @Override
    public List<ExpenseGroup> findAll() {
        return expenseGroupJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<ExpenseGroup> findById(UUID id) {
        return expenseGroupJpaRepository.findById(id)
                .map(this::toDomain);
    }

    private ExpenseGroup toDomain(ExpenseGroupEntity entity) {
        ExpenseGroupId id = ExpenseGroupId.from(entity.getId());
        GroupName groupName = GroupName.withName(entity.getName());
        List<ParticipantEntity> participantEntities = participantJpaRepository.findByGroupIdOrderByEmail(entity.getId());
        if (participantEntities.isEmpty()) {
            Participant creator = Participant.withEmail(entity.getCreatedBy());
            return ExpenseGroup.reconstitute(id, groupName, creator, entity.isActivated());
        }
        List<Participant> participants = participantEntities.stream()
                .map(pe -> Participant.reconstitute(
                        ParticipantId.from(pe.getParticipantId()),
                        pe.getEmail(),
                        Money.fromBalance(pe.getBalanceAmount())))
                .toList();
        return ExpenseGroup.reconstitute(id, groupName, entity.getCreatedBy(), participants, entity.isActivated());
    }
}
