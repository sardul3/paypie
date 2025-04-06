package io.github.sardul3;

import io.github.sardul3.account.domain.ParticipantEmail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("Participant Email | should be normalized to lowercase")
    public void participantEmailShouldBeNormalizedToLower(String emailAddress) {
        final String expectedNormalizedEmail = emailAddress.toLowerCase();

        ParticipantEmail participantEmail = ParticipantEmail.of(emailAddress);
        assertEquals(expectedNormalizedEmail, participantEmail.getEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {"us.com", "s@com", "user", "", "@."})
    @DisplayName("Participant Email | should reject invalid email addresses")
    void participantEmailShouldNotBeAnInvalidEmailFormat(String emailAddress) {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> ParticipantEmail.of(emailAddress));

        assertThat(exception.getMessage())
                .isNotBlank()
                .contains("Invalid email format");
    }
    @Test
    @DisplayName("Participant Email | should reject invalid email addresses")
    void participantEmailShouldNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> ParticipantEmail.of(null));
    }

}
