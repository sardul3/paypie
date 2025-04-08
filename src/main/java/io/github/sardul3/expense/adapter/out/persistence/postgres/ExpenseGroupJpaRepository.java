package io.github.sardul3.expense.adapter.out.persistence.postgres;

import io.github.sardul3.expense.domain.model.ExpenseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseGroupJpaRepository extends JpaRepository<ExpenseGroupEntity, UUID> {
    boolean existsByName(String name);
}
