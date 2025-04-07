package io.github.sardul3.expense.domain.common.annotation;

import java.lang.annotation.*;

/**
 * Indicates that a class is a Domain Entity in Domain-Driven Design (DDD).
 *
 * <p><strong>What is a Domain Entity?</strong></p>
 * A Domain Entity represents a uniquely identifiable object that exists and evolves
 * over time within a specific bounded context. Unlike Value Objects, which are
 * identified solely by their attributes, Entities are distinguished by their identity.
 *
 * <p><strong>Key Characteristics of Entities:</strong></p>
 * <ul>
 *   <li><strong>Identity:</strong> Each entity has a unique and persistent identity (e.g., UUID, natural key)</li>
 *   <li><strong>Mutable state:</strong> Entities can change their internal state over time while preserving identity</li>
 *   <li><strong>Domain behavior:</strong> Entities encapsulate domain logic and enforce business rules</li>
 *   <li><strong>Equality by identity:</strong> Two entities are equal if they share the same identity, regardless of internal state</li>
 *   <li><strong>May be aggregate root:</strong> An entity may act as the root of an aggregate, enforcing consistency boundaries</li>
 * </ul>
 *
 * <p><strong>Common Responsibilities:</strong></p>
 * <ul>
 *   <li>Enforce business invariants during lifecycle (creation, updates, deletion)</li>
 *   <li>Coordinate operations across value objects and other entities</li>
 *   <li>Own lifecycle and identity across persistent boundaries (e.g., databases, APIs)</li>
 * </ul>
 *
 * <p><strong>Implementation Guidelines:</strong></p>
 * <ul>
 *   <li>Declare a unique ID field (`final` or explicitly set during construction)</li>
 *   <li>Override <code>equals()</code> and <code>hashCode()</code> using only the identity field(s)</li>
 *   <li>Prefer immutability where possible; allow mutability intentionally and locally</li>
 *   <li>Expose behavior via meaningful methods (not setters)</li>
 *   <li>Avoid leaking persistence concerns (e.g., JPA annotations) into the domain model</li>
 *   <li>Keep side effects (e.g., event publishing) within the domain boundary if applicable</li>
 * </ul>
 *
 * <p><strong>Examples:</strong></p>
 * <ul>
 *   <li><code>Participant</code> - An entity with a unique ID and balance tracking</li>
 *   <li><code>User</code> - An entity with roles, preferences, and a password hash</li>
 *   <li><code>Order</code> - Aggregate root that coordinates LineItems and status transitions</li>
 * </ul>
 *
 * <p><strong>Anti-patterns to Avoid:</strong></p>
 * <ul>
 *   <li>Using business fields (like email or name) for equality</li>
 *   <li>Allowing direct field mutation via public setters</li>
 *   <li>Embedding persistence annotations in the domain model</li>
 *   <li>Placing validation logic in services instead of entities</li>
 * </ul>
 *
 * <p><strong>References:</strong></p>
 * <ul>
 *   <li>Evans, Eric. <em>Domain-Driven Design: Tackling Complexity in the Heart of Software</em></li>
 *   <li>Vernon, Vaughn. <em>Implementing Domain-Driven Design</em></li>
 *   <li><a href="https://dddcommunity.org/">https://dddcommunity.org/</a></li>
 * </ul>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DomainEntity {

    /**
     * A domain-focused description of what this entity represents.
     *
     * Example: "Represents a participant in a group-based expense tracking system"
     */
    String description() default "";

    /**
     * The bounded context where this entity belongs.
     *
     * Example: "expense-management", "identity-access", "order-fulfillment"
     */
    String boundedContext() default "";

    /**
     * Whether this entity acts as an aggregate root in its context.
     *
     * Aggregate roots enforce consistency and are the entry point to a cluster of domain objects.
     */
    boolean isAggregateRoot() default false;
}

