# 📁 ADR-005: Participant Equality by ID, Email Uniqueness Enforced via Aggregate

**Status:** Accepted  
**Date:** 2025-04-06

## Context
Each `Participant` has a UUID and an email. While UUID is the entity’s identity, email must be unique **within a group** as a business invariant. Including email in equality checks violates DDD principles.

## Decision
- Implement `equals()` and `hashCode()` using `id` only.
- Model email uniqueness as an **invariant** in the `ExpenseGroup` aggregate.
- `ParticipantEmail` remains a value object with normalization and validation rules.

## Consequences
- Maintains consistent entity identity logic.
- Avoids bugs in hash-based collections due to mutable fields.
- Enforces critical business rules at the domain level, not infrastructure.
- Keeps value object principles intact.
