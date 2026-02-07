package io.github.sardul3.expense.adapter.out.persistence.postgres.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Composite primary key for expense_group_participants.
 */
public class GroupParticipantId implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID groupId;
    private UUID participantId;

    public GroupParticipantId() {
    }

    public GroupParticipantId(UUID groupId, UUID participantId) {
        this.groupId = groupId;
        this.participantId = participantId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupParticipantId that = (GroupParticipantId) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(participantId, that.participantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, participantId);
    }
}
