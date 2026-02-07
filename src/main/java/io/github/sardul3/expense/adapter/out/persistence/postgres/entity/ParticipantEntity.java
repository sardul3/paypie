package io.github.sardul3.expense.adapter.out.persistence.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * JPA entity for expense_group_participants (participants per group with balance).
 */
@Entity
@Table(name = "expense_group_participants")
@IdClass(GroupParticipantId.class)
public class ParticipantEntity {

    @Id
    @Column(name = "group_id")
    private UUID groupId;

    @Id
    @Column(name = "participant_id")
    private UUID participantId;

    @Column(nullable = false)
    private String email;

    @Column(name = "balance_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAmount = BigDecimal.ZERO;

    public ParticipantEntity() {
    }

    public ParticipantEntity(UUID groupId, UUID participantId, String email, BigDecimal balanceAmount) {
        this.groupId = groupId;
        this.participantId = participantId;
        this.email = email;
        this.balanceAmount = balanceAmount != null ? balanceAmount : BigDecimal.ZERO;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public UUID getParticipantId() {
        return participantId;
    }

    public void setParticipantId(UUID participantId) {
        this.participantId = participantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount != null ? balanceAmount : BigDecimal.ZERO;
    }
}
