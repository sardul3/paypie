# 📁 ADR-003: Create Custom `@ValueObject` Annotation for Domain Semantics

**Status:** Accepted
**Date:** 2025-04-06

## Context
Domain primitives like email and group names are modeled as value objects. To clearly indicate these classes are value-based and to support future static analysis or documentation, we propose a custom `@ValueObject` annotation.

## Decision
Introduce a custom annotation:

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueObject {
String description();
String boundedContext();
}
```

It will be used to annotate domain types representing immutable, value-based constructs.

## Consequences
- Improves documentation and clarity in the domain model.
- Provides a hook for tooling or metadata extraction.
- No behavioral impact or runtime cost.
