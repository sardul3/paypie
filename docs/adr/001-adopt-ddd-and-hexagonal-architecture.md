# 📁ADR-001: Adopt Domain-Driven Design (DDD) and Hexagonal Architecture

**Status:** Accepted  
**Date:** 2025-04-06

## Context
The system being developed is domain-centric, involving core business logic around participants and expense groups. The architecture must support isolation of business rules, high testability, and long-term maintainability. We aim to prevent infrastructure concerns from leaking into core logic.

## Decision
Adopt **Domain-Driven Design (DDD)** and **Hexagonal Architecture (Ports and Adapters)**:
- Place business logic in the domain layer using entities, value objects, and aggregates.
- Separate application use cases via input/output ports.
- Implement ports via adapters for web, persistence, or external systems.

## Consequences
- Clean separation of responsibilities.
- Testable domain logic without Spring or databases.
- Flexible infrastructure; swap technologies without impacting the core.
- Encourages better modeling of business rules and invariants.
