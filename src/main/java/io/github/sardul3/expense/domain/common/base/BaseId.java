package io.github.sardul3.expense.domain.common.base;

import java.util.Objects;

/**
 * Generic type-safe wrapper for domain identity.
 */
public abstract class BaseId<ID> {
    private final ID id;

    protected BaseId(ID id) {
        this.id = Objects.requireNonNull(id, "ID value cannot be null");
    }

    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return Objects.equals(id, baseId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BaseId{" +
                "id=" + id +
                '}';
    }
}
