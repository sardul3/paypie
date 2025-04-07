package io.github.sardul3.account.domain;

/**
 * Marker base class for aggregate roots.
 * Will support domain events and lifecycle metadata in the future.
 */
public abstract class BaseAggregateRoot<ID extends BaseId<?>> extends BaseEntity<ID> {

    protected BaseAggregateRoot(ID id) {
        super(id);
    }

    //TODO: Future enhancement: domain event recording, auditing, etc.
}
