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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    public ID getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}
