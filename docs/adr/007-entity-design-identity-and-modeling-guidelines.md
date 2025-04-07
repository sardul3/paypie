# 📁 ADR-007: Entity Design, Identity Modeling, and Factory-Based Creation

**Status:** Accepted  
**Date:** 2025-04-06

---

### **Context**

Domain entities in our system, such as `Participant`, must have clearly defined identities, lifecycle boundaries, and behavior. We've introduced annotation-driven clarity (`@DomainEntity`) and updated design standards to enforce these principles consistently.

---

### **Decision**

- Entities must be marked with `@DomainEntity`, with `description`, `boundedContext`, and `isAggregateRoot`.
- Identity (`UUID id`) is the **only factor** for equality and hashCode.
- Public constructors are discouraged. All entities are created using **factory methods** (e.g., `Participant.withEmail()`).
- Business attribute-based equality (e.g., by email) is explicitly disallowed.
- State like `balance` in entities is mutable **only through intention-revealing domain methods**.

---

### **Additional Modeling Standards**

- Null-check all public method arguments in entities.
- Validate invariants like balance thresholds at the **entity or aggregate** level, not in value objects.
- Avoid JPA or persistence annotations inside the domain model (adapters only).
- All equality comparisons in tests must rely on `UUID` identity, never internal fields.

---

### **Consequences**

- Enforces DDD's identity semantics for entities.
- Improves clarity and safety when modeling aggregates.
- Factory methods align entity creation with domain language.
- Prevents subtle bugs caused by mutable fields or incorrect equality definitions.

---

### **References**

- `Participant.java`
- `ParticipantTest.java` (equality, construction, identity tests)
- `@DomainEntity` annotation file

---
