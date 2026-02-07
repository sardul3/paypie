package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.dto.SettleUpCommand;
import io.github.sardul3.expense.application.dto.SettleUpResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SettleUpServiceTest {

    private ExpenseGroupRepository expenseGroupRepository;
    private SettleUpService settleUpService;

    @BeforeEach
    void setUp() {
        expenseGroupRepository = mock(ExpenseGroupRepository.class);
        settleUpService = new SettleUpService(expenseGroupRepository);
    }

    @Test
    @DisplayName("should record settlement and save group")
    void shouldRecordSettlementAndSaveGroup() {
        Participant alice = Participant.withEmail("alice@example.com");
        Participant bob = Participant.withEmail("bob@example.com");
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), alice);
        group.addParticipant(bob);
        UUID groupId = group.getId().getId();

        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(expenseGroupRepository.save(any(ExpenseGroup.class))).thenAnswer(inv -> inv.getArgument(0));

        SettleUpResponse response = settleUpService.settleUp(groupId,
                new SettleUpCommand(bob.getParticipantId().getId(), alice.getParticipantId().getId(), BigDecimal.TEN));

        assertThat(response.groupId()).isEqualTo(groupId);
        verify(expenseGroupRepository).save(group);
        // Payer (bob) is credited, receiver (alice) is debited
        assertThat(bob.getBalance()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(alice.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(-10));
    }

    @Test
    @DisplayName("should throw ExpenseGroupNotFoundException when group not found")
    void shouldThrowWhenGroupNotFound() {
        UUID groupId = UUID.randomUUID();
        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> settleUpService.settleUp(groupId,
                new SettleUpCommand(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.ONE)))
                .isInstanceOf(ExpenseGroupNotFoundException.class)
                .hasMessageContaining("Expense group not found");
    }
}
