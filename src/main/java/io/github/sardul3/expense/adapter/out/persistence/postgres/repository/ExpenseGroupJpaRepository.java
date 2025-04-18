package io.github.sardul3.expense.adapter.out.persistence.postgres.repository;

import io.github.sardul3.expense.adapter.out.persistence.postgres.entity.ExpenseGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseGroupJpaRepository extends JpaRepository<ExpenseGroupEntity, UUID> {
    boolean existsByName(String name);
}
