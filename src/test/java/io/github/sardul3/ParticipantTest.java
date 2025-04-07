package io.github.sardul3;

import io.github.sardul3.account.domain.Money;
import io.github.sardul3.account.domain.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
    @DisplayName("Participant | should be deemed equal if and only if they have same IDs")
    void participantEntitiesWithSameEmailsAreNotConsideredEqual() {
        final String email = "user@comp.com";

        Participant participant = Participant.withEmail(email);
        Participant anotherParticipant = Participant.withEmail(email);
        assertThat(participant).isNotEqualTo(anotherParticipant);
    }

    @Test
    @DisplayName("Participant | new participant should have zero balance")
    void newlyCreatedParticipantShouldStartWithZeroBalance() {
        final String email = "user@comp.com";
        Participant participant = Participant.withEmail(email);
        assertEquals(0, participant.getBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Participant | participants should have the ability to add credit")
    void participantShouldBeAbleToCreditAccount() {
        final String email = "user@comp.com";
        final int money = 11;
        Participant participant = Participant.withEmail(email);
        Money creditMoney = Money.of(new BigDecimal(money));
        participant.credit(creditMoney);

        assertThat(participant.getBalance()).isEqualByComparingTo("11.00");
        assertEquals(0, participant.getBalance().compareTo(BigDecimal.valueOf(money)));
    }


}
