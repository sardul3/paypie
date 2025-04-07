package io.github.sardul3.expense.application.common.annotation;

import java.lang.annotation.*;

/**
 * Marks a class as an Application Input Port in Clean/Hexagonal Architecture.
 *
 * <p><strong>What is an Input Port?</strong></p>
 * An input port defines a use case interface that is invoked by outside actors (e.g., controllers, APIs).
 * It abstracts the application logic and is implemented by a Use Case class (annotated with <code>@UseCase</code>).
 *
 * <p><strong>Conventions:</strong></p>
 * <ul>
 *   <li>Interface-only (no implementation)</li>
 *   <li>Located under <code>application.port.in</code></li>
 *   <li>Receives command objects, returns response DTOs</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * @InputPort(description = "Handles command to create a new expense group")
 * public interface CreateExpenseGroupUseCase {
 *     CreateExpenseGroupResponse createExpenseGroup(CreateExpenseGroupCommand command);
 * }
 * }
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InputPort {
    String description() default "";
}
