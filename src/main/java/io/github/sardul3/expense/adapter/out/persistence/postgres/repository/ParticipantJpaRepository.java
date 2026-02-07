package io.github.sardul3.expense.adapter.out.persistence.postgres.repository;

import io.github.sardul3.expense.adapter.out.persistence.postgres.entity.GroupParticipantId;
import io.github.sardul3.expense.adapter.out.persistence.postgres.entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParticipantJpaRepository extends JpaRepository<ParticipantEntity, GroupParticipantId> {

    List<ParticipantEntity> findByGroupIdOrderByEmail(UUID groupId);

    void deleteByGroupId(UUID groupId);
}
