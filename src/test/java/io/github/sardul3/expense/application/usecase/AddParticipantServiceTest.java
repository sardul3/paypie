package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.dto.AddParticipantCommand;
import io.github.sardul3.expense.application.dto.AddParticipantResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.exception.ParticipantAlreadyInGroupException;
import io.github.sardul3.expense.application.port.out.DomainEventPublisher;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.event.ParticipantAddedEvent;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddParticipantServiceTest {

    private ExpenseGroupRepository expenseGroupRepository;
    private DomainEventPublisher domainEventPublisher;
    private AddParticipantService addParticipantService;

    @BeforeEach
    void setUp() {
        expenseGroupRepository = mock(ExpenseGroupRepository.class);
        domainEventPublisher = mock(DomainEventPublisher.class);
        addParticipantService = new AddParticipantService(expenseGroupRepository, domainEventPublisher);
    }

    @Test
    @DisplayName("should add participant and return response when group exists")
    void shouldAddParticipantWhenGroupExists() {
        Participant creator = Participant.withEmail("alice@example.com");
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), creator);
        UUID groupId = group.getId().getId();

        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(expenseGroupRepository.save(any(ExpenseGroup.class))).thenAnswer(inv -> inv.getArgument(0));

        AddParticipantResponse response = addParticipantService.addParticipant(
                groupId,
                new AddParticipantCommand("bob@example.com")
        );

        assertThat(response.email()).isEqualTo("bob@example.com");
        assertThat(response.participantId()).isNotNull();
        verify(expenseGroupRepository).save(group);
        assertThat(group.getParticipants()).hasSize(2);
    }

    @Test
    @DisplayName("should throw ExpenseGroupNotFoundException when group does not exist")
    void shouldThrowWhenGroupNotFound() {
        UUID groupId = UUID.randomUUID();
        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addParticipantService.addParticipant(
                groupId,
                new AddParticipantCommand("bob@example.com")))
                .isInstanceOf(ExpenseGroupNotFoundException.class)
                .hasMessageContaining("Expense group not found");
    }

    @Test
    @DisplayName("should throw ParticipantAlreadyInGroupException when email already in group")
    void shouldThrowWhenParticipantAlreadyInGroup() {
        Participant creator = Participant.withEmail("alice@example.com");
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), creator);
        group.addParticipant(Participant.withEmail("bob@example.com"));
        UUID groupId = group.getId().getId();

        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        assertThatThrownBy(() -> addParticipantService.addParticipant(
                groupId,
                new AddParticipantCommand("bob@example.com")))
                .isInstanceOf(ParticipantAlreadyInGroupException.class)
                .hasMessageContaining("already in the group");
    }

    @Test
    @DisplayName("should treat creator email as duplicate")
    void shouldTreatCreatorEmailAsDuplicate() {
        Participant creator = Participant.withEmail("alice@example.com");
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), creator);
        UUID groupId = group.getId().getId();

        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        assertThatThrownBy(() -> addParticipantService.addParticipant(
                groupId,
                new AddParticipantCommand("alice@example.com")))
                .isInstanceOf(ParticipantAlreadyInGroupException.class);
    }

    @Test
    @DisplayName("should publish ParticipantAddedEvent when participant is added")
    void shouldPublishParticipantAddedEventWhenParticipantAdded() {
        Participant creator = Participant.withEmail("alice@example.com");
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), creator);
        UUID groupId = group.getId().getId();

        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(expenseGroupRepository.save(any(ExpenseGroup.class))).thenAnswer(inv -> inv.getArgument(0));

        addParticipantService.addParticipant(groupId, new AddParticipantCommand("bob@example.com"));

        verify(domainEventPublisher).publish(argThat(event ->
                event instanceof ParticipantAddedEvent e
                        && e.groupId().equals(groupId)
                        && e.email().equals("bob@example.com")
                        && e.participantId() != null));
    }

    @Test
    @DisplayName("should be case-insensitive for duplicate email check")
    void shouldBeCaseInsensitiveForDuplicateEmail() {
        Participant creator = Participant.withEmail("alice@example.com");
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), creator);
        group.addParticipant(Participant.withEmail("bob@example.com"));
        UUID groupId = group.getId().getId();

        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        assertThatThrownBy(() -> addParticipantService.addParticipant(
                groupId,
                new AddParticipantCommand("BOB@example.com")))
                .isInstanceOf(ParticipantAlreadyInGroupException.class);
    }

    @Nested
    @DisplayName("AddParticipant use case | Edge cases and validation")
    class EdgeCasesAndValidation {

        @Test
        @DisplayName("When email in command is null, throw IllegalArgumentException with message about email")
        void whenEmailIsNull_throwIllegalArgumentException() {
            Participant creator = Participant.withEmail("alice@example.com");
            ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), creator);
            UUID groupId = group.getId().getId();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

            assertThatThrownBy(() -> addParticipantService.addParticipant(groupId, new AddParticipantCommand(null)))
                    .isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class);
        }

        @Test
        @DisplayName("When email is blank, throw IllegalArgumentException")
        void whenEmailIsBlank_throwIllegalArgumentException() {
            Participant creator = Participant.withEmail("alice@example.com");
            ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), creator);
            UUID groupId = group.getId().getId();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

            assertThatThrownBy(() -> addParticipantService.addParticipant(groupId, new AddParticipantCommand("   ")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("email");
        }

        @Test
        @DisplayName("When groupId is null, throw rather than proceed")
        void whenGroupIdIsNull_throwRatherThanProceed() {
            assertThatThrownBy(() -> addParticipantService.addParticipant(null, new AddParticipantCommand("bob@example.com")))
                    .satisfies(t -> assertThat(t).isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class, ExpenseGroupNotFoundException.class));
        }

        @Test
        @DisplayName("When command is null, throw rather than NPE in call stack")
        void whenCommandIsNull_throwRatherThanNPE() {
            UUID groupId = UUID.randomUUID();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> addParticipantService.addParticipant(groupId, null))
                    .satisfies(t -> assertThat(t).isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class));
        }
    }
}
