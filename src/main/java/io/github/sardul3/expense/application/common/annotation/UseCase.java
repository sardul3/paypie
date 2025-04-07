package io.github.sardul3.expense.application.common.annotation;

import java.lang.annotation.*;

/**
 * Indicates that a class is an Application Use Case in the context of Hexagonal or Clean Architecture.
 *
 * <p><strong>What is a Use Case?</strong></p>
 * A use case represents a specific business operation that orchestrates the application logic. It coordinates
 * input ports, output ports, domain logic, and may emit responses. It should not contain domain logic itself,
 * but should delegate to domain models or services.
 *
 * <p><strong>Typical Responsibilities:</strong></p>
 * <ul>
 *   <li>Accept input via command DTOs</li>
 *   <li>Validate and delegate to domain models or aggregates</li>
 *   <li>Persist state via output ports</li>
 *   <li>Return responses using response DTOs</li>
 * </ul>
 *
 * <p><strong>Conventions:</strong></p>
 * <ul>
 *   <li>Belongs in the <code>application.usecase</code> package</li>
 *   <li>Implements an interface annotated with <code>@InputPort</code></li>
 *   <li>Should have no knowledge of external infrastructure (DBs, frameworks)</li>
 * </ul>
 *
 * <p><strong>Best Practices:</strong></p>
 * <ul>
 *   <li>Use constructor injection to receive dependencies (ports)</li>
 *   <li>Throw domain-relevant exceptions only (e.g., <code>ExpenseGroupAlreadyExistsException</code>)</li>
 *   <li>Return clean response DTOs — avoid leaking domain models</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * @UseCase(
 *   description = "Creates a new expense group after validation",
 *   inputPort = CreateExpenseGroupUseCase.class
 * )
 * public class CreateExpenseGroupService implements CreateExpenseGroupUseCase {
 *   ...
 * }
 * }
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UseCase {
    String description() default "";
    Class<?> inputPort();
}

