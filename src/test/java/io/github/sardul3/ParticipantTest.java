package io.github.sardul3;

import io.github.sardul3.account.domain.Money;
import io.github.sardul3.account.domain.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ParticipantEmail | Entity Behavior")
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

    @Test
    @DisplayName("Participant | money should be placed with 2 decimal places")
    void participantMoneyShouldBeUsedWithTwoDecimalPlaces() {
        final String email = "user@comp.com";
        final int money = 11;
        Participant participant = Participant.withEmail(email);
        Money creditMoney = Money.of(new BigDecimal(money));
        participant.credit(creditMoney);

        assertThat(participant.getBalance()).isEqualByComparingTo("11.00");
        assertEquals(0, participant.getBalance().compareTo(BigDecimal.valueOf(money)));
    }

    @Test
    @DisplayName("Participant | participants should have the ability to perform debit operation")
    void participantShouldBeAbleToDebitAccount() {
        final String email = "user@comp.com";
        final int money = 11;
        Participant participant = Participant.withEmail(email);
        Money creditMoney = Money.of(new BigDecimal(money));
        participant.credit(creditMoney);
        participant.debit(creditMoney);

        assertThat(participant.getBalance()).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("Participant | participants cannot debit if it results in negative amount")
    void participantShouldNotBeAbleToDebitAccountIfItLeadsToNegativeAmount() {
        final String email = "user@comp.com";
        final int money = 11;
        Participant participant = Participant.withEmail(email);
        Money creditMoney = Money.of(new BigDecimal(money));
        assertThrows(IllegalStateException.class, () -> participant.debit(creditMoney));
    }

    @Test
    @DisplayName("Participant | participants cannot credit amount with value ZERO ($0)")
    void participantShouldNotBeAbleToCreditZeroAmount() {
        final String email = "user@comp.com";
        final int money = 0;
        Participant participant = Participant.withEmail(email);
        Money creditMoney = Money.of(new BigDecimal(money));
        assertThrows(IllegalArgumentException.class, () -> participant.credit(creditMoney));
    }

    @Test
    @DisplayName("Participant | participants cannot debit amount with value ZERO ($0)")
    void participantShouldNotBeAbleToDebitZeroAmount() {
        final String email = "user@comp.com";
        final int money = 0;
        Participant participant = Participant.withEmail(email);
        Money creditMoney = Money.of(new BigDecimal(money));
        assertThrows(IllegalArgumentException.class, () -> participant.debit(creditMoney));
    }
}
