
# 📁ADR-012: Application Layer Structure and Design Strategy

**Status:** Accepted  
**Date:** 2025-04-07  
**Author:** Sardul3  
**Context:** Expense Management – PayPie Project

---

## 1. Overview

This document captures the architectural decisions, conventions, and guidelines for structuring and implementing the **Application Layer** in our DDD-based system using **Hexagonal (Ports and Adapters)** and **Clean Architecture** principles.

The Application Layer serves as the **intermediary between the external world and the domain model**. It defines use case-specific logic by coordinating domain model operations, validating inputs, invoking domain services (if any), and persisting the results via ports.

This ADR is intended as a **complete guide** for both new and experienced engineers to implement the application layer in a scalable, testable, and production-ready way.

---

## 2. Purpose of the Application Layer

The **Application Layer**:

- **Defines use cases** that orchestrate domain behavior
- **Receives commands** from external sources (e.g., controllers, schedulers)
- **Invokes domain logic** through aggregates, entities, and services
- **Persists domain objects** via output ports
- **Returns responses** using DTOs that abstract domain internals

It does **not**:
- Contain business logic (business rules are in domain model)
- Interact directly with infrastructure (e.g., database, HTTP, messaging)
- Handle serialization, HTTP binding, or persistence annotations

---

## 3. Responsibilities

| Responsibility | Description |
|----------------|-------------|
| **Input Coordination** | Accept validated command/request objects and delegate to domain |
| **Validation (optional)** | Perform basic checks (null, formatting) before touching domain |
| **Domain Interaction** | Call aggregates or domain services to fulfill the use case |
| **Persistence Orchestration** | Use output ports to store/retrieve domain entities |
| **Output Conversion** | Convert aggregates to response DTOs if needed |

---

## 4. Folder and Package Structure

```
src/main/java/io/github/sardul3/expense/application
├── dto/                  # Input (Command) and Output (Response) records
│   ├── CreateExpenseGroupCommand.java
│   └── CreateExpenseGroupResponse.java
│
├── port/
│   ├── in/               # Input ports (use case contracts)
│   │   └── CreateExpenseGroupUseCase.java
│   └── out/              # Output ports (driven adapters)
│       └── ExpenseGroupRepository.java
│
├── usecase/              # Application service implementations (use case logic)
│   └── CreateExpenseGroupService.java
│
└── exception/            # Application-specific exceptions
    └── ExpenseGroupAlreadyExistsException.java
```

---

## 5. Architectural Elements

### 5.1 Input Ports (`port.in`)

Interfaces that define **use case contracts**. These represent the **application boundary** through which external actors (UI, APIs) interact.

Example:
```java
@InputPort(description = "Handles the creation of new expense groups")
public interface CreateExpenseGroupUseCase {
    CreateExpenseGroupResponse createExpenseGroup(CreateExpenseGroupCommand command);
}
```

### 5.2 Output Ports (`port.out`)

Interfaces that define required **infrastructure behavior** (repositories, publishers). The **application layer depends on these abstractions**, not implementations.

Example:
```java
@OutputPort(description = "Abstraction for persisting and querying ExpenseGroups")
public interface ExpenseGroupRepository {
    boolean existsByName(GroupName groupName);
    ExpenseGroup save(ExpenseGroup expenseGroup);
}
```

### 5.3 Use Cases (`usecase/`)

Classes that implement input ports and **orchestrate** domain operations.

Example:
```java
@UseCase(description = "Implements the logic to create an expense group", inputPort = CreateExpenseGroupUseCase.class)
public class CreateExpenseGroupService implements CreateExpenseGroupUseCase {
    ...
}
```

### 5.4 DTOs (`dto/`)

Immutable records used to **decouple the domain model** from external requests/responses. These do **not** leak domain logic or internal fields.

Example:
```java
public record CreateExpenseGroupCommand(String name, String createdBy) {}

public record CreateExpenseGroupResponse(UUID id, String name) {
    public CreateExpenseGroupResponse(ExpenseGroupId id, GroupName groupName) {
        this(id.getId(), groupName.getName());
    }
}
```

---

## 6. Design Principles and Best Practices

| Principle | Guideline |
|----------|-----------|
| **Separation of concerns** | Application layer only coordinates, never owns domain logic |
| **Port-based programming** | All input/output operations must go through interfaces |
| **DTO boundaries** | Commands and Responses are separate from domain models |
| **Value Object usage** | Always pass domain objects (e.g., GroupName) in output ports instead of raw primitives |
| **No mapper logic in application** | Avoid domain logic inside mappers; they only convert objects |
| **Testability** | All use cases should be independently unit testable |
| **No framework leakage** | Spring or persistence annotations should not exist in application or domain layers |

---

## 7. Common Pitfalls and Fixes

| Concern | Recommendation |
|--------|----------------|
| Domain logic in mapper | Move to domain model or use case |
| `String` instead of VO in repository | Use `GroupName` instead of `String` |
| Use case returns domain object | Return a DTO or Response object instead |
| Null/blank inputs not validated | Validate in DTO or VO constructor |
| Service placed in wrong package | Use `usecase/` for service classes, not `port/` |

---

## 8. Benefits of This Structure

- Encourages testability and modularity
- Ensures business logic is not scattered
- Supports adapters like REST, CLI, Kafka, or Scheduled Jobs without changes to the core
- Complies with DDD building blocks (Aggregate, Entity, VO, Service)
- Infrastructure code can evolve independently
- Encourages consistency and readability across the team

---

## 9. Example Flow

1. Controller calls `CreateExpenseGroupUseCase#createExpenseGroup`
2. Application layer validates command
3. Checks for group existence via `ExpenseGroupRepository.existsByName()`
4. Constructs domain model: `ExpenseGroup.from(...)`
5. Persists via `ExpenseGroupRepository.save(...)`
6. Returns `CreateExpenseGroupResponse`

---

## 10. Additional Conventions

- `@InputPort`, `@OutputPort`, and `@UseCase` annotations are mandatory
- DTOs must be records (immutable)
- Exceptions used must be specific, descriptive, and domain-aligned
- Application services must not throw `RuntimeException`, `IllegalArgumentException`, or other generic exceptions unless wrapping them

---

## 11. References

- Evans, Eric. *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Vernon, Vaughn. *Implementing Domain-Driven Design*
- Clean Architecture (Robert C. Martin)
- Hexagonal Architecture (Alistair Cockburn)

---

## 12. Status

This application layer design is **fully implemented** for the Expense Group use case. It serves as a reference implementation for all future use cases.

