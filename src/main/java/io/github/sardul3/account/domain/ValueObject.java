package io.github.sardul3.account.domain;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a class is a Value Object in Domain-Driven Design (DDD).
 *
 * <p><strong>What is a Value Object?</strong></p>
 * A Value Object is a fundamental building block in Domain-Driven Design. Unlike entities
 * which are defined by their identity (like a Person with an ID), value objects are defined
 * entirely by their attributes/values (like Money with amount and currency).
 *
 * <p><strong>Key Characteristics:</strong></p>
 * <ul>
 *   <li><strong>Identity-less:</strong> Two value objects with the same attributes are considered equal</li>
 *   <li><strong>Immutable:</strong> Cannot be changed after creation; any modification creates a new instance</li>
 *   <li><strong>Self-validating:</strong> Ensures its data is valid during construction</li>
 *   <li><strong>Equality-comparable:</strong> Must implement equals() and hashCode() based on all attributes</li>
 *   <li><strong>Side-effect free:</strong> Operations on value objects should return new instances</li>
 * </ul>
 *
 * <p><strong>Examples:</strong></p>
 * <ul>
 *   <li>Money (amount, currency)</li>
 *   <li>DateRange (start date, end date)</li>
 *   <li>Address (street, city, postal code, country)</li>
 *   <li>PhoneNumber (country code, number)</li>
 * </ul>
 *
 * <p><strong>Implementation Guidelines:</strong></p>
 * <ul>
 *   <li>Make all fields final (immutability)</li>
 *   <li>Validate all input in the constructor</li>
 *   <li>Implement equals() and hashCode()</li>
 *   <li>Provide factory methods for clearer construction</li>
 *   <li>Consider implementing Comparable if ordering is meaningful</li>
 *   <li>Override toString() for readable debugging</li>
 * </ul>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ValueObject {
    /**
     * Optional description of what this value object represents in the domain model.
     *
     * Example: For a PhoneNumber value object, the description might be:
     * "Represents a valid phone number with country code and validation"
     */
    String description() default "";

    /**
     * The bounded context this value object belongs to.
     *
     * <p>A bounded context is a specific business area or subdomain in your application
     * with its own ubiquitous language and model. Specifying the bounded context helps
     * clarify where this value object fits in the larger system.</p>
     *
     * Example: "user-management", "payment-processing", "inventory"
     */
    String boundedContext() default "";
}
