package io.github.sardul3.account.domain;

import java.lang.annotation.*;

/**
 * Indicates that a class is an Aggregate Root in Domain-Driven Design (DDD).
 *
 * <p><strong>What is an Aggregate Root?</strong></p>
 * An Aggregate Root is the main entry point into an aggregate. It coordinates access to
 * the aggregate's internal state and ensures that domain invariants are consistently enforced.
 *
 * <p>Aggregates are clusters of associated objects that are treated as a single unit for data changes.
 * The Aggregate Root acts as a gateway and enforcer of all business rules that span across the aggregate.</p>
 *
 * <p><strong>Key Characteristics:</strong></p>
 * <ul>
 *   <li><strong>Single point of entry:</strong> Only the aggregate root may be referenced or interacted with externally</li>
 *   <li><strong>Consistency boundary:</strong> Changes to the internal state of the aggregate must go through the root</li>
 *   <li><strong>Entity base:</strong> An aggregate root is a special type of entity with identity and lifecycle</li>
 *   <li><strong>Transaction boundary:</strong> The entire aggregate is typically loaded, validated, and saved as one unit</li>
 * </ul>
 *
 * <p><strong>Responsibilities of an Aggregate Root:</strong></p>
 * <ul>
 *   <li>Coordinate creation, updates, and removal of internal entities or value objects</li>
 *   <li>Validate and enforce business invariants for the entire aggregate</li>
 *   <li>Emit domain events on significant state transitions</li>
 *   <li>Prevent unauthorized access to internal members (no leaking internals)</li>
 * </ul>
 *
 * <p><strong>Design Guidelines:</strong></p>
 * <ul>
 *   <li>Should extend <code>BaseAggregateRoot</code> which itself extends <code>BaseEntity</code></li>
 *   <li>Should expose only necessary operations via intention-revealing methods</li>
 *   <li>Should reject invalid state transitions at the root level</li>
 *   <li>Should not expose mutable references to internal entities or collections</li>
 *   <li>Should be persisted and retrieved as a whole unit</li>
 * </ul>
 *
 * <p><strong>Examples:</strong></p>
 * <ul>
 *   <li><code>ExpenseGroup</code> - Manages participant membership and group metadata</li>
 *   <li><code>Order</code> - Coordinates order items, pricing, and status transitions</li>
 *   <li><code>Project</code> - Controls lifecycle and assignment of clustered work units</li>
 * </ul>
 *
 * <p><strong>Anti-patterns to Avoid:</strong></p>
 * <ul>
 *   <li>Exposing inner entities or collections directly without encapsulation</li>
 *   <li>Allowing updates to entities within the aggregate bypassing the root</li>
 *   <li>Letting external services perform validation that belongs in the aggregate root</li>
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
public @interface AggregateRoot {

    /**
     * Optional description of what this aggregate root manages or protects.
     * @return A human-readable description of this aggregate root's responsibility.
     * Example: "Represents a group of participants collaborating on shared expenses"
     */
    String description() default "";

    /**
     * The bounded context in which this aggregate root exists.
     *
     * Example: "expense-management", "order-processing", "user-provisioning"
     */
    String boundedContext() default "";
}
