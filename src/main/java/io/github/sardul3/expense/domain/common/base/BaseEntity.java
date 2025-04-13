package io.github.sardul3.expense.domain.common.base;

import java.util.Objects;

/**
 * Base class for all domain entities.
 * Provides identity-based equality and shared infrastructure.
 */
public abstract class BaseEntity<ID extends BaseId<?>> {
    protected final ID id;

    protected BaseEntity(ID id) {
        this.id = Objects.requireNonNull(id, "Entity ID cannot be null");
    }

    public ID getId() {
        return id;
    }

}
