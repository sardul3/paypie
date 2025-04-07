# 📁ADR-002: Define Project Structure Using Clean Architecture

**Status:** Accepted  
**Date:** 2025-04-06

## Context
The current folder structure is:

```
src/main/java/io/github/sardul3/account
├── adapter
│   ├── in.web
│   └── out.persistence
├── application.port
│   ├── in
│   └── out
└── domain
```

This structure maps Clean and Hexagonal Architecture principles, separating domain logic, application orchestration, and infrastructure-specific concerns.

## Decision
We adopt this structure to align with Clean Architecture:
- `domain`: Holds all business logic (entities, value objects, aggregates).
- `application.port.in`: Defines incoming use cases or commands.
- `application.port.out`: Defines outbound interfaces like repositories.
- `adapter.in.web`: Handles HTTP requests.
- `adapter.out.persistence`: Connects to database or storage mechanisms.

## Consequences
- Promotes clear boundaries and inversion of control.
- Improves maintainability and testability.
- Aligns code organization with architectural roles.