package io.github.sardul3.expense.application.port.out;

import io.github.sardul3.expense.application.common.annotation.OutputPort;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.valueobject.GroupName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port: persistence for expense groups. Implementations may use JPA, in-memory, etc.
 */
@OutputPort(
        description = "Abstracts the persistence of Expense Groups",
        role = OutputPort.Role.PERSISTENCE
)
public interface ExpenseGroupRepository {
    /** Returns true if a group with the given name already exists. */
    boolean existsByName(GroupName groupName);

    /** Persists the aggregate; returns the same instance. */
    ExpenseGroup save(ExpenseGroup expenseGroup);

    /** Returns all expense groups (reconstituted from storage). */
    List<ExpenseGroup> findAll();

    /** Returns the expense group with the given id if present. */
    Optional<ExpenseGroup> findById(UUID id);
}
