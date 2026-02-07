package io.github.sardul3.expense.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Settlement | Value Object")
class SettlementTest {

    @Test
    @DisplayName("should create valid settlement between two participants")
    void shouldCreateValidSettlement() {
        ParticipantId from = ParticipantId.generate();
        ParticipantId to = ParticipantId.generate();
        Money amount = Money.of(BigDecimal.TEN);

        Settlement settlement = Settlement.of(from, to, amount);

        assertThat(settlement.getFromParticipantId()).isEqualTo(from);
        assertThat(settlement.getToParticipantId()).isEqualTo(to);
        assertThat(settlement.getAmount().getAmount()).isEqualByComparingTo(BigDecimal.TEN);
    }

    @Test
    @DisplayName("should reject same participant for from and to")
    void shouldRejectSameParticipant() {
        ParticipantId same = ParticipantId.generate();
        Money amount = Money.of(BigDecimal.ONE);

        assertThatThrownBy(() -> Settlement.of(same, same, amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("different participants");
    }

    @Test
    @DisplayName("should reject zero amount")
    void shouldRejectZeroAmount() {
        ParticipantId from = ParticipantId.generate();
        ParticipantId to = ParticipantId.generate();

        assertThatThrownBy(() -> Settlement.of(from, to, Money.withZeroBalance()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    @Test
    @DisplayName("should reject null from participant")
    void shouldRejectNullFrom() {
        ParticipantId to = ParticipantId.generate();
        assertThatThrownBy(() -> Settlement.of(null, to, Money.of(BigDecimal.ONE)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should reject null to participant")
    void shouldRejectNullTo() {
        ParticipantId from = ParticipantId.generate();
        assertThatThrownBy(() -> Settlement.of(from, null, Money.of(BigDecimal.ONE)))
                .isInstanceOf(NullPointerException.class);
    }
}
