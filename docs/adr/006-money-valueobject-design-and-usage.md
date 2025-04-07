
# 📁ADR-006: Design and Usage of `Money` as a Domain Value Object

**Status:** Accepted  
**Date:** 2025-04-06

---

### **Context**

In the `expense-management` bounded context, we require a robust representation for monetary values that respects precision, immutability, and arithmetic consistency. Currency-related logic must be isolated from entities to prevent primitive obsession and improve domain clarity.

---

### **Decision**

We introduced a `Money` value object with the following rules:
- **Immutable by design** (uses `final` fields and no setters)
- All operations (`add`, `subtract`) are **side-effect free** and return new instances
- **Scale is enforced** to exactly 2 decimal places using `RoundingMode.HALF_UP`
- Exposes semantic helpers like `isNegative()`, `isNotPositive()`
- Zero values can only be created via `Money.withZeroBalance()`

We decided **not to reject `$0.00` at the VO level**, but instead validate it at the **entity boundary** (`Participant.credit()` and `.debit()`).

---

### **Consequences**

- Reduces bugs caused by mutable monetary calculations.
- Encourages value object purity and aligns with DDD principles.
- Improves testability and arithmetic consistency.
- `$0` and negative value enforcement becomes **contextual**, supporting use cases where zero money is valid (e.g., reporting).

---

### **References**
- `MoneyTest.java` covers equality, rounding, arithmetic, immutability, and guardrails.
- `Participant.credit()` and `Participant.debit()` reject zero and negative `Money`.

---