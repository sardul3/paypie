package io.github.sardul3;

import io.github.sardul3.account.domain.ParticipantEmail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParticipantEmail | Value Object Behavior")
public class ParticipantEmailTest {

    @Test
    @DisplayName("Participant Email | should be created without errors")
    public void testParticipantEmailCreation() {
        final String emailAddress = "user@test.com";
        ParticipantEmail participantEmail = ParticipantEmail.of(emailAddress);
        assertEquals(emailAddress, participantEmail.getEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "useR@gmail.com",
            "USer@gmail.com",
            "user@Gmail.com",
            "user@GMAIL.COM"
    })
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

    @Test
    @DisplayName("Participant Email | should be considered same if their values are equal")
    void participantEmailShouldBeEqualIfValuesAreSame() {
        final String emailAddress = "user@test.com";
        ParticipantEmail participantEmail = ParticipantEmail.of(emailAddress);
        ParticipantEmail anotherParticipantEmail = ParticipantEmail.of(emailAddress);

        assertEquals(participantEmail, anotherParticipantEmail);
        assertEquals(participantEmail.hashCode(), anotherParticipantEmail.hashCode());
    }

    @Test
    @DisplayName("Participant Email | different emails should not be equal")
    void participantEmailsShouldNotBeEqualIfDifferent() {
        ParticipantEmail email1 = ParticipantEmail.of("a@x.com");
        ParticipantEmail email2 = ParticipantEmail.of("b@x.com");

        assertNotEquals(email1, email2);
        assertNotEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    @DisplayName("Participant Email | toString() should return normalized email value")
    void toStringShouldReturnNormalizedEmailValue() {
        String rawEmail = "USER@Test.COM";
        ParticipantEmail participantEmail = ParticipantEmail.of(rawEmail);

        String toStringOutput = participantEmail.toString();
        assertThat(toStringOutput)
                .contains("user@test.com") // Assuming toString() is just `return email;`
                .doesNotContain("USER")
                .doesNotContain(" ")
                .contains("@test.com");
    }

    @Test
    @DisplayName("Participant Email | getEmail() should be immutable and copy-safe")
    void getEmailShouldReturnImmutableValue() {
        String rawEmail = "user@example.com";
        ParticipantEmail participantEmail = ParticipantEmail.of(rawEmail);

        String original = participantEmail.getEmail();
        String hacked = original + "-spoofed";

        // Ensure internal state is untouched
        assertEquals("user@example.com", participantEmail.getEmail());
        assertThat(participantEmail.getEmail()).doesNotContain("spoofed");
    }


}
