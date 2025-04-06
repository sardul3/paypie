package io.github.sardul3;

import io.github.sardul3.account.domain.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParticipantTest {

    @Test
    @DisplayName("Participant | should be created with valid email")
    void participantShouldBeCreatedWithValidEmail() {
        final String email = "user@comp.com";
        Participant participant = Participant.withEmail(email);
        assertEquals(email, participant.getEmail());
    }
}
