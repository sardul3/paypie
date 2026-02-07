package io.github.sardul3.expense.adapter.out.persistence.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * JPA entity for expense_activities table.
 */
@Entity
@Table(name = "expense_activities")
public class ExpenseActivityEntity {

    @Id
    private UUID id;

    @Column(name = "group_id", nullable = false)
    private UUID groupId;

    @Column(nullable = false, length = 50)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "paid_by_participant_id", nullable = false)
    private UUID paidByParticipantId;

    @Column(name = "split_evenly", nullable = false)
    private boolean splitEvenly = true;

    public ExpenseActivityEntity() {
    }

    public ExpenseActivityEntity(UUID id, UUID groupId, String description, BigDecimal amount,
                                  UUID paidByParticipantId, boolean splitEvenly) {
        this.id = id;
        this.groupId = groupId;
        this.description = description;
        this.amount = amount;
        this.paidByParticipantId = paidByParticipantId;
        this.splitEvenly = splitEvenly;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getPaidByParticipantId() {
        return paidByParticipantId;
    }

    public void setPaidByParticipantId(UUID paidByParticipantId) {
        this.paidByParticipantId = paidByParticipantId;
    }

    public boolean isSplitEvenly() {
        return splitEvenly;
    }

    public void setSplitEvenly(boolean splitEvenly) {
        this.splitEvenly = splitEvenly;
    }
}
