package io.github.sardul3;

import io.github.sardul3.account.domain.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParticipantTest {

    @Test
    @DisplayName("Participant | should be created with valid email")
    void participantShouldBeCreatedWithValidEmail() {
        final String email = "user@comp.com";
        Participant participant = Participant.withEmail(email);
        assertEquals(email, participant.getEmail());
    }

    @Test
    @DisplayName("Participant | should be created with a new ID as it is an entity")
    void participantShouldBeCreatedWithAnEntityId() {
        final String email = "user@comp.com";
        Participant participant = Participant.withEmail(email);
        assertThat(participant.getId()).isNotNull();
    }

    @Test
    void participantEntitiesWithSameValuesAreNotEqual() {
        final String email = "user@comp.com";

        Participant participant = Participant.withEmail(email);
        Participant anotherParticipant = Participant.withEmail(email);
        assertThat(participant).isEqualTo(anotherParticipant);
    }
}
