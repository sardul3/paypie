package io.github.sardul3.expense.adapter.common;

import java.lang.annotation.*;

/**
 * Annotation to mark a class as a Primary Adapter in a Hexagonal Architecture.
 *
 * <p>
 * In Hexagonal Architecture (also known as Ports and Adapters), a Primary Adapter is a driving adapter
 * that initiates interactions with the application through input ports.
 * Examples include:
 * </p>
 * <ul>
 *     <li>HTTP Controllers (REST APIs)</li>
 *     <li>GraphQL Resolvers</li>
 *     <li>CLI Commands</li>
 *     <li>Event Consumers (e.g., Kafka Listeners)</li>
 * </ul>
 *
 * <p>
 * This annotation provides clarity to future developers and documentation generators
 * that this class acts as a boundary entry point, **orchestrating use case invocation**.
 * </p>
 *
 * <h2>Best Practices:</h2>
 * <ul>
 *     <li>Use with classes in the `adapter.in` package (e.g., `web`, `cli`, `graphql`, etc.)</li>
 *     <li>Should not contain any business logic</li>
 *     <li>Only coordinates request → validation → use case call → response formatting</li>
 *     <li>Always depend on **input ports**, never directly on domain logic</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 *
 * <pre>{@code
 * @PrimaryAdapter
 * @RestController
 * @RequestMapping("/api/v1/expense")
 * public class CreateExpenseGroupController {
 *     private final CreateExpenseGroupUseCase createExpenseGroupUseCase;
 * }
 * }</pre>
 *
 * @see io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrimaryAdapter {
}

