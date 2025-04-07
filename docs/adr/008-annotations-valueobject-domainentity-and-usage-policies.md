
#  📁 ADR-008: Semantic Annotations for Domain Clarity — `@ValueObject` and `@DomainEntity`

**Status:** Accepted  
**Date:** 2025-04-06

---

### **Context**

To make our domain model more **self-documenting** and **enforceable via static analysis**, we introduced two custom annotations:

- `@ValueObject` — for identity-less, immutable objects like `Money`, `ParticipantEmail`
- `@DomainEntity` — for identity-bound, behavior-rich entities like `Participant`

These annotations clarify model roles, assist in architectural validation, and enhance understanding across teams.

---

### **Decision**

#### `@ValueObject`
- Marks classes that are **immutable**, **self-validating**, and **side-effect free**
- Contains:
    - `description`: What the value represents
    - `boundedContext`: Where it belongs (e.g., `expense-management`)

#### `@DomainEntity`
- Marks classes that have **persistent identity** and **domain lifecycle**
- Contains:
    - `description`
    - `boundedContext`
    - `isAggregateRoot`: Whether it’s the root of a consistency boundary

---

### **Usage Rules**

- All Value Objects must:
    - Have final fields
    - Be constructed via factory methods
    - Validate inputs in constructors
    - Implement `equals` and `hashCode` based on attributes

- All Entities must:
    - Use identity (`UUID`) for equality
    - Expose only intentional mutators
    - Be created through descriptive static factories
    - Never use attribute-based equality

---

### **Benefits**

- Improves **readability** and **semantic clarity**
- Enables architectural validation tools (e.g., ArchUnit)
- Documents bounded context membership directly in code
- Guides new developers on how to extend the model safely

---

### **References**

- `ValueObject.java`
- `DomainEntity.java`
- `ParticipantEmail`, `Money`, `Participant` classes
- Vaughn Vernon’s _Implementing Domain-Driven Design_
- DDD Reference: [https://dddcommunity.org/](https://dddcommunity.org/)

---