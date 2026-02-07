package io.github.sardul3.expense.adapter.out.persistence.postgres.repository;

import io.github.sardul3.expense.adapter.out.persistence.postgres.entity.ExpenseActivityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseActivityJpaRepository extends JpaRepository<ExpenseActivityEntity, UUID> {

    List<ExpenseActivityEntity> findByGroupIdOrderByAmountDesc(UUID groupId);

    Page<ExpenseActivityEntity> findByGroupId(UUID groupId, Pageable pageable);

    void deleteByGroupId(UUID groupId);
}
