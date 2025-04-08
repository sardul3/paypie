package io.github.sardul3.expense.adapter.common;

import java.lang.annotation.*;

/**
 * Annotation to mark a class as a Secondary Adapter in a Hexagonal Architecture.
 *
 * <p>
 * In Hexagonal Architecture (also known as Ports and Adapters), a Secondary Adapter is a driven adapter
 * that the application interacts with through output ports.
 * Examples include:
 * </p>
 * <ul>
 *     <li>Database Repositories (e.g., JPA, MongoDB)</li>
 *     <li>External API Clients (e.g., REST clients, Feign)</li>
 *     <li>Messaging Producers (e.g., Kafka, RabbitMQ)</li>
 *     <li>File Writers, Blob Storage Handlers, etc.</li>
 * </ul>
 *
 * <p>
 * This annotation clarifies architectural boundaries and improves maintainability,
 * signaling that the annotated class fulfills infrastructure concerns **by implementing output ports**.
 * </p>
 *
 * <h2>Best Practices:</h2>
 * <ul>
 *     <li>Use with classes in the `adapter.out` package (e.g., `persistence`, `external`, `messaging`, etc.)</li>
 *     <li>Should only implement output ports</li>
 *     <li>Should contain infrastructure-related logic only (no business logic)</li>
 *     <li>Should be testable in isolation (often mocked in use case tests)</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * @SecondaryAdapter
 * @Repository
 * public class PostgresExpenseGroupRepository implements ExpenseGroupRepository {
 *     ...
 * }
 * }</pre>
 *
 * @see io.github.sardul3.expense.application.port.out.ExpenseGroupRepository
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecondaryAdapter {
}

