package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.dto.CreateExpenseActivityCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.exception.ParticipantNotFoundInGroupException;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateExpenseActivityServiceTest {

    private ExpenseGroupRepository expenseGroupRepository;
    private CreateExpenseActivityService service;

    ExpenseGroup expenseGroup;
    Participant groupCreator;
    Participant anotherParticipant;

    @BeforeEach
    void setUp() {
        expenseGroupRepository = mock(ExpenseGroupRepository.class);
        service = new CreateExpenseActivityService(expenseGroupRepository);

        groupCreator = Participant.withEmail("test@example.com");
        expenseGroup = ExpenseGroup.from(GroupName.withName("apt-group"), groupCreator);
         anotherParticipant = Participant.withEmail("test2@example.com");
        expenseGroup.addParticipant(anotherParticipant);

        expenseGroup.activate();
    }

    @Test
    void shouldCreateExpenseActivity() {
        when(expenseGroupRepository.findById(any())).thenReturn(Optional.ofNullable(expenseGroup));

        CreateExpenseActivityCommand createExpenseActivityCommand = new CreateExpenseActivityCommand(
            expenseGroup.getId().getId(),
                "food - doordash",
                BigDecimal.valueOf(150.00),
                groupCreator.getParticipantId().getId(),
                List.of(groupCreator.getParticipantId().getId(), anotherParticipant.getParticipantId().getId())
        );

        var expenseActivity =  service.createExpenseActivity(createExpenseActivityCommand);
        assertThat(expenseActivity)
                .extracting(CreateExpenseActivityResponse::payerBalance)
                .isEqualTo(BigDecimal.valueOf(7500, 2));

        assertThat(expenseGroup)
                .extracting(ExpenseGroup::getActivities)
                .asList()
                .hasSize(1);

        assertThat(anotherParticipant)
                .extracting(Participant::getBalance)
                .isEqualTo(BigDecimal.valueOf(-7500, 2));

    }

    @Test
    void shouldCreateExpenseActivityWithRightSplitForMultipleMembers() {
        Participant thirdParticipant = Participant.withEmail("test3@example.com");
        expenseGroup.addParticipant(thirdParticipant);

        when(expenseGroupRepository.findById(any())).thenReturn(Optional.ofNullable(expenseGroup));

        CreateExpenseActivityCommand createExpenseActivityCommand = new CreateExpenseActivityCommand(
                expenseGroup.getId().getId(),
                "food - doordash",
                BigDecimal.valueOf(300),
                groupCreator.getParticipantId().getId(),
                List.of(groupCreator.getParticipantId().getId(), anotherParticipant.getParticipantId().getId()
                , thirdParticipant.getParticipantId().getId())
        );

        var expenseActivity =  service.createExpenseActivity(createExpenseActivityCommand);
        assertThat(expenseActivity)
                .extracting(CreateExpenseActivityResponse::payerBalance)
                .isEqualTo(BigDecimal.valueOf(20000, 2));

        assertThat(expenseGroup)
                .extracting(ExpenseGroup::getActivities)
                .asList()
                .hasSize(1);

        assertThat(thirdParticipant)
                .extracting(Participant::getBalance)
                .isEqualTo(BigDecimal.valueOf(-10000, 2));

    }

    @Nested
    @DisplayName("CreateExpenseActivity use case | Edge cases and validation")
    class EdgeCasesAndValidation {

        @Test
        @DisplayName("When group does not exist, throw ExpenseGroupNotFoundException with group id in message")
        void whenGroupDoesNotExist_throwExpenseGroupNotFoundException() {
            UUID groupId = UUID.randomUUID();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.empty());
            CreateExpenseActivityCommand command = new CreateExpenseActivityCommand(
                    groupId, "Coffee", BigDecimal.TEN, groupCreator.getParticipantId().getId(), null);

            assertThatThrownBy(() -> service.createExpenseActivity(command))
                    .isInstanceOf(ExpenseGroupNotFoundException.class)
                    .hasMessageContaining("not found")
                    .hasMessageContaining(groupId.toString());
        }

        @Test
        @DisplayName("When paidBy participant is not in group, throw ParticipantNotFoundInGroupException")
        void whenPayerNotInGroup_throwParticipantNotFoundInGroupException() {
            UUID unknownParticipantId = UUID.randomUUID();
            when(expenseGroupRepository.findById(expenseGroup.getId().getId())).thenReturn(Optional.of(expenseGroup));
            CreateExpenseActivityCommand command = new CreateExpenseActivityCommand(
                    expenseGroup.getId().getId(), "Lunch", BigDecimal.valueOf(20), unknownParticipantId, null);

            assertThatThrownBy(() -> service.createExpenseActivity(command))
                    .isInstanceOf(ParticipantNotFoundInGroupException.class)
                    .hasMessageContaining("Participant not found");
        }

        @Test
        @DisplayName("When description is null, throw IllegalArgumentException with clear message")
        void whenDescriptionIsNull_throwIllegalArgumentException() {
            when(expenseGroupRepository.findById(any())).thenReturn(Optional.of(expenseGroup));
            CreateExpenseActivityCommand command = new CreateExpenseActivityCommand(
                    expenseGroup.getId().getId(), null, BigDecimal.TEN, groupCreator.getParticipantId().getId(), null);

            assertThatThrownBy(() -> service.createExpenseActivity(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("description");
        }

        @Test
        @DisplayName("When description is blank, throw IllegalArgumentException")
        void whenDescriptionIsBlank_throwIllegalArgumentException() {
            when(expenseGroupRepository.findById(any())).thenReturn(Optional.of(expenseGroup));
            CreateExpenseActivityCommand command = new CreateExpenseActivityCommand(
                    expenseGroup.getId().getId(), "   ", BigDecimal.TEN, groupCreator.getParticipantId().getId(), null);

            assertThatThrownBy(() -> service.createExpenseActivity(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("When amount is zero, throw IllegalArgumentException with message about positive amount")
        void whenAmountIsZero_throwIllegalArgumentException() {
            when(expenseGroupRepository.findById(any())).thenReturn(Optional.of(expenseGroup));
            CreateExpenseActivityCommand command = new CreateExpenseActivityCommand(
                    expenseGroup.getId().getId(), "Item", BigDecimal.ZERO, groupCreator.getParticipantId().getId(), null);

            assertThatThrownBy(() -> service.createExpenseActivity(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("positive");
        }

        @Test
        @DisplayName("When amount is negative, throw IllegalArgumentException")
        void whenAmountIsNegative_throwIllegalArgumentException() {
            when(expenseGroupRepository.findById(any())).thenReturn(Optional.of(expenseGroup));
            CreateExpenseActivityCommand command = new CreateExpenseActivityCommand(
                    expenseGroup.getId().getId(), "Item", BigDecimal.valueOf(-5), groupCreator.getParticipantId().getId(), null);

            assertThatThrownBy(() -> service.createExpenseActivity(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("When group is not activated, throw clear exception before adding activity")
        void whenGroupNotActivated_throwBeforeAddingActivity() {
            ExpenseGroup inactiveGroup = ExpenseGroup.from(GroupName.withName("inactive"), groupCreator);
            inactiveGroup.addParticipant(anotherParticipant);
            when(expenseGroupRepository.findById(inactiveGroup.getId().getId())).thenReturn(Optional.of(inactiveGroup));
            CreateExpenseActivityCommand command = new CreateExpenseActivityCommand(
                    inactiveGroup.getId().getId(), "Lunch", BigDecimal.TEN, groupCreator.getParticipantId().getId(), null);

            assertThatThrownBy(() -> service.createExpenseActivity(command))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("activated");
        }

        @Test
        @DisplayName("When splitWith contains participant ID not in group, throw clear exception")
        void whenSplitWithContainsNonMember_throwClearException() {
            UUID nonMemberId = UUID.randomUUID();
            when(expenseGroupRepository.findById(any())).thenReturn(Optional.of(expenseGroup));
            CreateExpenseActivityCommand command = new CreateExpenseActivityCommand(
                    expenseGroup.getId().getId(), "Dinner", BigDecimal.valueOf(100),
                    groupCreator.getParticipantId().getId(),
                    List.of(groupCreator.getParticipantId().getId(), nonMemberId));

            assertThatThrownBy(() -> service.createExpenseActivity(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("When description exceeds max length, throw IllegalArgumentException")
        void whenDescriptionExceedsMaxLength_throwIllegalArgumentException() {
            when(expenseGroupRepository.findById(any())).thenReturn(Optional.of(expenseGroup));
            String longDesc = "a".repeat(51);
            CreateExpenseActivityCommand command = new CreateExpenseActivityCommand(
                    expenseGroup.getId().getId(), longDesc, BigDecimal.TEN, groupCreator.getParticipantId().getId(), null);

            assertThatThrownBy(() -> service.createExpenseActivity(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("longer");
        }
    }
}