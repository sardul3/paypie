package io.github.sardul3.expense.application.common.annotation;

import java.lang.annotation.*;

/**
 * Marks an interface as an Output Port (i.e., gateway to external or domain services).
 *
 * <p><strong>What is an Output Port?</strong></p>
 * An output port abstracts communication with the outside world (database, queues, 3rd-party APIs),
 * allowing the use case to depend on an interface and not on implementation details.
 *
 * <p><strong>Conventions:</strong></p>
 * <ul>
 *   <li>Located under <code>application.port.out</code></li>
 *   <li>Interface-only (infrastructure provides implementation)</li>
 *   <li>Should be written in domain terms (not JPA/DB-specific)</li>
 * </ul>
 *
 * <p><strong>Best Practices:</strong></p>
 * <ul>
 *   <li>Do not expose infrastructure-specific return types (like <code>JpaEntity</code>)</li>
 *   <li>Use value objects instead of primitive types where possible</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * @OutputPort(description = "Repository to store and query Expense Groups")
 * public interface ExpenseGroupRepository {
 *     boolean existsByName(GroupName name);
 *     ExpenseGroup save(ExpenseGroup group);
 * }
 * }
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OutputPort {
    String description() default "";

    enum Role {
        PERSISTENCE, CACHE, EVENT_PUBLISHER, FILE_STORAGE, EXTERNAL_API
    }

    Role role() default Role.PERSISTENCE;
}

