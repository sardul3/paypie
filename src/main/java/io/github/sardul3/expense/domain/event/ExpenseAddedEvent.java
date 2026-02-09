package io.github.sardul3.expense.domain.event;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Domain event raised when an expense activity is added to a group.
 *
 * @param groupId             expense group id
 * @param description         expense description
 * @param amount              expense amount
 * @param paidByParticipantId  participant who paid
 */
public record ExpenseAddedEvent(UUID groupId, String description, BigDecimal amount, UUID paidByParticipantId)
        implements DomainEvent {
}
