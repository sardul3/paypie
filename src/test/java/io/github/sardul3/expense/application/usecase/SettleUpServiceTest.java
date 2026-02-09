package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.dto.SettleUpCommand;
import io.github.sardul3.expense.application.dto.SettleUpResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.out.DomainEventPublisher;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.event.SettlementCompletedEvent;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SettleUpServiceTest {

    private ExpenseGroupRepository expenseGroupRepository;
    private DomainEventPublisher domainEventPublisher;
    private SettleUpService settleUpService;

    @BeforeEach
    void setUp() {
        expenseGroupRepository = mock(ExpenseGroupRepository.class);
        domainEventPublisher = mock(DomainEventPublisher.class);
        settleUpService = new SettleUpService(expenseGroupRepository, domainEventPublisher);
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
    @DisplayName("should publish SettlementCompletedEvent when settlement recorded")
    void shouldPublishSettlementCompletedEventWhenSettlementRecorded() {
        Participant alice = Participant.withEmail("alice@example.com");
        Participant bob = Participant.withEmail("bob@example.com");
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), alice);
        group.addParticipant(bob);
        UUID groupId = group.getId().getId();

        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(expenseGroupRepository.save(any(ExpenseGroup.class))).thenAnswer(inv -> inv.getArgument(0));

        settleUpService.settleUp(groupId,
                new SettleUpCommand(bob.getParticipantId().getId(), alice.getParticipantId().getId(), BigDecimal.TEN));

        verify(domainEventPublisher).publish(argThat(event ->
                event instanceof SettlementCompletedEvent e
                        && e.groupId().equals(groupId)
                        && e.fromParticipantId().equals(bob.getParticipantId().getId())
                        && e.toParticipantId().equals(alice.getParticipantId().getId())
                        && e.amount().compareTo(BigDecimal.TEN) == 0));
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

    @Nested
    @DisplayName("SettleUp use case | Edge cases and validation")
    class EdgeCasesAndValidation {

        @Test
        @DisplayName("When fromParticipantId is not in group, throw with message indicating payer not in group")
        void whenPayerNotInGroup_throwWithClearMessage() {
            Participant alice = Participant.withEmail("alice@example.com");
            Participant bob = Participant.withEmail("bob@example.com");
            ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), alice);
            group.addParticipant(bob);
            UUID groupId = group.getId().getId();
            UUID unknownPayerId = UUID.randomUUID();

            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

            assertThatThrownBy(() -> settleUpService.settleUp(groupId,
                    new SettleUpCommand(unknownPayerId, alice.getParticipantId().getId(), BigDecimal.TEN)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("not in group");
        }

        @Test
        @DisplayName("When toParticipantId is not in group, throw with message indicating receiver not in group")
        void whenReceiverNotInGroup_throwWithClearMessage() {
            Participant alice = Participant.withEmail("alice@example.com");
            Participant bob = Participant.withEmail("bob@example.com");
            ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), alice);
            group.addParticipant(bob);
            UUID groupId = group.getId().getId();
            UUID unknownReceiverId = UUID.randomUUID();

            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

            assertThatThrownBy(() -> settleUpService.settleUp(groupId,
                    new SettleUpCommand(bob.getParticipantId().getId(), unknownReceiverId, BigDecimal.TEN)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("not in group");
        }

        @Test
        @DisplayName("When amount is zero, throw IllegalArgumentException")
        void whenAmountIsZero_throwIllegalArgumentException() {
            Participant alice = Participant.withEmail("alice@example.com");
            Participant bob = Participant.withEmail("bob@example.com");
            ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), alice);
            group.addParticipant(bob);
            UUID groupId = group.getId().getId();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

            assertThatThrownBy(() -> settleUpService.settleUp(groupId,
                    new SettleUpCommand(bob.getParticipantId().getId(), alice.getParticipantId().getId(), BigDecimal.ZERO)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("positive");
        }

        @Test
        @DisplayName("When amount is negative, throw IllegalArgumentException")
        void whenAmountIsNegative_throwIllegalArgumentException() {
            Participant alice = Participant.withEmail("alice@example.com");
            Participant bob = Participant.withEmail("bob@example.com");
            ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), alice);
            group.addParticipant(bob);
            UUID groupId = group.getId().getId();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

            assertThatThrownBy(() -> settleUpService.settleUp(groupId,
                    new SettleUpCommand(bob.getParticipantId().getId(), alice.getParticipantId().getId(), BigDecimal.valueOf(-5))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("When from and to are the same participant, throw IllegalArgumentException")
        void whenFromEqualsTo_throwIllegalArgumentException() {
            Participant alice = Participant.withEmail("alice@example.com");
            Participant bob = Participant.withEmail("bob@example.com");
            ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), alice);
            group.addParticipant(bob);
            UUID groupId = group.getId().getId();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

            assertThatThrownBy(() -> settleUpService.settleUp(groupId,
                    new SettleUpCommand(alice.getParticipantId().getId(), alice.getParticipantId().getId(), BigDecimal.ONE)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("different participants");
        }

        @Test
        @DisplayName("When groupId is null, throw IllegalArgumentException or NullPointerException with clear contract")
        void whenGroupIdIsNull_throwRatherThanProceed() {
            assertThatThrownBy(() -> settleUpService.settleUp(null,
                    new SettleUpCommand(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.ONE)))
                    .satisfies(t -> assertThat(t).isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class));
        }

        @Test
        @DisplayName("When command is null, throw rather than NPE deep in call stack")
        void whenCommandIsNull_throwRatherThanNPE() {
            UUID groupId = UUID.randomUUID();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> settleUpService.settleUp(groupId, null))
                    .satisfies(t -> assertThat(t).isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class));
        }
    }
}
