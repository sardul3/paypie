package io.github.sardul3.expense.domain.valueobject;

import io.github.sardul3.expense.domain.common.base.BaseId;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ParticipantIdTest {

    @Test
    void shouldHaveConsistentID() {
        UUID id = UUID.randomUUID();
        ParticipantId  participantId = ParticipantId.from(id);
        assertThat(participantId.getId())
                .isEqualTo(id);

    }

    @Test
    void shouldCompareUsingIds() {
        UUID id = UUID.randomUUID();
        ParticipantId  participantId = ParticipantId.from(id);
        ParticipantId  participantId2 = ParticipantId.from(id);
        assertThat(participantId)
                .isEqualTo(participantId2);

    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        UUID id = UUID.randomUUID();
        ParticipantId  participantId = ParticipantId.from(id);
        ParticipantId  participantId2 = null;
        assertThat(participantId).isNotEqualTo(participantId2);
    }

    @Test
    void shouldReturnFalseWhenComparedWithDifferentType() {
        UUID id = UUID.randomUUID();
        ParticipantId  participantId = ParticipantId.from(id);

        assertThat(participantId).isNotEqualTo(id);
    }

    @Test
    void shouldHaveSameHashCodeForSameIds() {
        UUID id = UUID.randomUUID();
        ParticipantId participantId1 = ParticipantId.from(id);
        ParticipantId participantId2 = ParticipantId.from(id);
        assertThat(participantId1.hashCode()).isEqualTo(participantId2.hashCode());
    }

    @Test
    void shouldHaveToStringContainingId() {
        UUID id = UUID.randomUUID();
        ParticipantId participantId = ParticipantId.from(id);
        assertThat(participantId.toString()).contains(id.toString());
    }

    @Test
    void shouldReturnFalseForDifferentBaseIdSubclass() {
        UUID id = UUID.randomUUID();
        ParticipantId participantId = ParticipantId.from(id);
        // Simulate another BaseId subclass
        ExpenseGroupId otherId = ExpenseGroupId.generate();
        assertThat(participantId).isNotEqualTo(otherId);
    }

}