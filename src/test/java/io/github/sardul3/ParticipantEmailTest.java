package io.github.sardul3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParticipantEmailTest {
    @Test
    @DisplayName("Participant Email | should be created without errors")
    public void testParticipantEmailCreation() {
        final String emailAddress = "user@test.com";
        ParticipantEmail participantEmail = ParticipantEmail.of(emailAddress);
        assertEquals(emailAddress, participantEmail.getEmail());
    }
}
