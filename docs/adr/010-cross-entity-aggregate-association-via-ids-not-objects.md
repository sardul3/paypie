# 📁 ADR-010: Model `ExpenseGroup`-to-`Participant` Association Using Identifiers Only, Not Full Aggregates

**Status:** Accepted  
**Date:** 2025-04-06  
**Author:** Sardul3  
**Applies To:** All aggregate-to-aggregate relationships across bounded contexts  
**Scope:** Expense management domain; aggregate interaction modeling

---

### Context

In the domain of collaborative expense tracking, an `ExpenseGroup` maintains a list of participants who can add or be associated with expenses. A design decision was needed to determine **how `ExpenseGroup` should reference participants**:

- Should `ExpenseGroup` maintain **references to full `Participant` aggregates**?
- Or should it only store **`ParticipantId` values** and resolve the aggregate externally when needed?

This decision directly impacts **aggregate boundaries**, **system modularity**, **data consistency**, and **application scalability**.

---

### Decision

We will model the relationship between `ExpenseGroup` and `Participant` using **only the `ParticipantId`**, not the full `Participant` object. The `ExpenseGroup` will store a list of `ParticipantId` references and treat participants as external aggregates.

```java
public class ExpenseGroup extends BaseAggregateRoot<ExpenseGroupId> {

    private final Set<ParticipantId> participantIds = new HashSet<>();

    public void addParticipant(ParticipantId participantId) {
        if (participantIds.contains(participantId)) {
            throw new IllegalArgumentException("Participant already exists in this group.");
        }
        if (participantIds.size() >= MAX_PARTICIPANTS) {
            throw new IllegalStateException("Group limit exceeded.");
        }
        participantIds.add(participantId);
    }

    public boolean hasParticipant(ParticipantId participantId) {
        return participantIds.contains(participantId);
    }
}
```

---

### Rationale

#### 1. Respects Aggregate Boundaries
Per DDD principles, aggregates should:
- Encapsulate their own state
- Not expose or directly manipulate the internals of other aggregates
  Passing or storing full `Participant` objects would violate these principles.

#### 2. Avoids Inconsistency from Stale State
Participants may change over time (e.g., email updates or balances). If `ExpenseGroup` were to cache or store full `Participant` objects, stale or duplicate state could emerge.

#### 3. Improves Scalability and Modularity
In a modular monolith or microservices setup, aggregates often reside in different modules or services. Using identifiers allows decoupling and facilitates communication across service boundaries.

#### 4. Enables Clean Persistence
Only storing `ParticipantId` makes the `ExpenseGroup` table or document lean, normalized, and easier to query and index.

#### 5. Supports Richer Composition via Application Layer
If business logic requires data from both aggregates, it can be handled in the **application service layer** or by a dedicated **domain service**, not by coupling the aggregates.

---

### Example: Coordinating Aggregates via Domain Service

```java
public class ExpenseGroupService {

    private final ExpenseGroupRepository groupRepo;
    private final ParticipantRepository participantRepo;

    public void assignParticipantToGroup(UUID groupId, UUID participantId) {
        ExpenseGroup group = groupRepo.findById(groupId);
        Participant participant = participantRepo.findById(participantId);

        group.addParticipant(participant.getId());
        participant.markAssignedTo(group.getId());

        groupRepo.save(group);
        participantRepo.save(participant);
    }
}
```

---

### Pros

| Benefit                           | Description |
|-----------------------------------|-------------|
| **Strong Encapsulation**          | Keeps each aggregate responsible for its own rules and lifecycle |
| **Clear Boundaries**              | Simplifies modeling, persistence, and testing |
| **Supports Microservices**        | Prevents cross-service coupling |
| **Reduces Redundancy**            | Avoids duplicating or caching full aggregate state |
| **Improves Modifiability**        | Aggregates can evolve independently |
| **Enables Event-Driven Integration** | Future interactions can be event-based via domain events using IDs only |

---

### Trade-offs

| Limitation                        | Mitigation |
|----------------------------------|------------|
| Cannot directly invoke behavior on `Participant` | Use domain services or application services |
| Requires ID resolution when needed | Use repositories or query views (CQRS-style read models) |

---

### Alternatives Considered

| Option                             | Rejected? | Reason |
|-----------------------------------|-----------|--------|
| Store full `Participant` in `ExpenseGroup` | Yes | Violates DDD aggregate boundaries, introduces data duplication |
| Maintain bidirectional references | Yes | Creates cyclic dependencies and tight coupling |
| Use a richer object like `ParticipantSummary` | Deferred | May be useful for projections, but not for core domain modeling |

---

### Consequences

- Domain model remains clean, maintainable, and extensible
- Enables future growth toward event-driven and service-oriented architecture
- Developers must manage cross-aggregate operations intentionally via services
- Queries will need ID resolution for enriched data (preferably via projections)

---

### References

- Evans, Eric. *Domain-Driven Design* (Chapter 6: Aggregates)
- Vernon, Vaughn. *Implementing Domain-Driven Design*
- https://dddcommunity.org
- Current class: `ExpenseGroup`, `Participant`, `ExpenseGroupService`

---

### Next Steps

- Refactor `ExpenseGroup` to use `ParticipantId` only
- Update factories and constructors to accept only identifiers
- Create integration tests to verify aggregate behavior via IDs
- Document domain services that coordinate aggregate behavior

