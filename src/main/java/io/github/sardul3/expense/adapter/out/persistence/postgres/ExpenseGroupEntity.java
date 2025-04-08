package io.github.sardul3.expense.adapter.out.persistence.postgres;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "expense_groups")
public class ExpenseGroupEntity {

    @Id
    private UUID id;

    private String name;
    private String createdBy;
    private boolean activated;

    public ExpenseGroupEntity() {
    }

    public ExpenseGroupEntity(UUID id) {
        this.id = id;
    }

    private ExpenseGroupEntity(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setCreatedBy(builder.createdBy);
        setActivated(builder.activated);
    }

    public static Builder newBuilder(ExpenseGroupEntity copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.name = copy.getName();
        builder.createdBy = copy.getCreatedBy();
        builder.activated = copy.isActivated();
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }


    public static final class Builder {
        private UUID id;
        private String name;
        private String createdBy;
        private boolean activated;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder id(UUID val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder createdBy(String val) {
            createdBy = val;
            return this;
        }

        public Builder activated(boolean val) {
            activated = val;
            return this;
        }

        public ExpenseGroupEntity build() {
            return new ExpenseGroupEntity(this);
        }
    }
}
