# 📁ADR-004: Tag Domain Value Objects Using `@ValueObject`

**Status:** Accepted  
**Date:** 2025-04-06

## Context
The domain contains types like `ParticipantEmail` and `GroupName`, which:
- Are immutable
- Have no identity
- Are compared by value

They should be explicitly marked as value objects.

## Decision
Use the custom `@ValueObject` annotation to tag the following classes:
- `ParticipantEmail`: Represents normalized, validated email addresses.
- `GroupName`: Represents trimmed and validated group names.

## Consequences
- Clarifies design intent to developers and code readers.
- Helps maintain DDD discipline.
- Prevents primitive obsession and encourages strong typing.
