package io.github.sardul3;

import io.github.sardul3.account.domain.ParticipantEmail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParticipantEmailTest {

    @Test
    @DisplayName("Participant Email | should be created without errors")
    public void testParticipantEmailCreation() {
        final String emailAddress = "user@test.com";
        ParticipantEmail participantEmail = ParticipantEmail.of(emailAddress);
        assertEquals(emailAddress, participantEmail.getEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {"useR@gmail.com", "USer@gmail.com", "user@Gmail.com", "user@GMAIL.COM"})
    public void participantEmailShouldBeNormalizedToLower(String emailAddress) {
        final String expectedNormalizedEmail = emailAddress.toLowerCase();

        ParticipantEmail participantEmail = ParticipantEmail.of(emailAddress);
        assertEquals(expectedNormalizedEmail, participantEmail.getEmail());
    }
}
