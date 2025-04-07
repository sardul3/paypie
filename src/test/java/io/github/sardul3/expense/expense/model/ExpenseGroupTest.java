package io.github.sardul3.expense.expense.model;

import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import io.github.sardul3.expense.domain.valueobject.ParticipantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ExpenseGroup | Aggregate Root Behavior")
public class ExpenseGroupTest {

    @Test
    @DisplayName("Expense Group | should create a valid expense group")
    void expenseGroupShouldBeCreatedWithCorrectName() {
        GroupName groupName = GroupName.withName("demo");
        ExpenseGroup expenseGroup = ExpenseGroup.from(
                "demo",
                Participant.withEmail("creator@example.com")
        );

        assertThat(expenseGroup.getGroupName())
                .isEqualTo(groupName);
    }

    @Test
    @DisplayName("Expense Group | should create a group with creator included")
    void expenseGroupCreationShouldIncludeTheGroupCreator() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("creator@example.com");
        Participant participant = Participant.withEmail("user@example.com");
        ExpenseGroup expenseGroup = ExpenseGroup.from(
                "demo", createdBy
        );

        assertThat(expenseGroup.getGroupName())
                .isNotNull()
                .isEqualTo(groupName);

        assertThat(expenseGroup.isActivated())
                .isFalse();
    }

    @Test
    @DisplayName("Expense Group | should enable adding other participants")
    void expenseGroupShouldAllowAdditionalParticipants() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("creator@example.com");
        Participant participant = Participant.withEmail("user@example.com");

        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName.getName(), createdBy
        );

        ParticipantId participantId = ParticipantId.from(participant.getId());
        expenseGroup.addParticipant(participantId);

        assertThat(expenseGroup.getParticipants().size())
                .isEqualTo(2);
    }

    @Test
    @DisplayName("Expense Group | should add group creator as a participant by default")
    void expenseGroupShouldAddCreatorAsParticipantByDefault() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("user@example.com");

        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName.getName(), createdBy
        );

        assertThat(expenseGroup.getParticipants().size())
                .isEqualTo(1);

        assertThat(expenseGroup.getParticipants().get(0).getId())
                .isEqualTo(createdBy.getParticipantId().getId());
    }

    @Test
    @DisplayName("Expense Group | should have unique participants per group including creator")
    void expenseGroupShouldNotAddDuplicateParticipants() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("creator@example.com");
        Participant participant = Participant.withEmail("user@example.com");

        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName.getName(), createdBy
        );

        ParticipantId participantId = ParticipantId.from(createdBy.getId());
        assertThrows(IllegalArgumentException.class, () -> expenseGroup.addParticipant(participantId));
    }

    @Test
    @DisplayName("Expense Group | should have unique participants per group")
    void expenseGroupShouldNotAddDuplicateParticipantsMultipleParticipants() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("creator@example.com");
        Participant participant = Participant.withEmail("user@example.com");
        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName.getName(), createdBy
        );

        ParticipantId participantId = ParticipantId.from(participant.getId());
        expenseGroup.addParticipant(participantId);
        assertThrows(IllegalArgumentException.class, () -> expenseGroup.addParticipant(participantId));
    }

    @Test
    @DisplayName("Expense Group | cannot be activated if it does not have at least 2 members")
    void expenseGroupCannotBeActivatedBeforeTwoTotalMembers() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("user@example.com");

        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName.getName(), createdBy
        );

        assertThrows(IllegalStateException.class, () -> expenseGroup.activate());
    }

    @Test
    @DisplayName("Expense Group | can be activated if it has at least 2 members")
    void expenseGroupCanBeActivatedAfterTwoTotalMembers() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("user@example.com");

        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName.getName(), createdBy
        );

        Participant participant = Participant.withEmail("user@example.com");
        ParticipantId participantId = ParticipantId.from(participant.getId());
        expenseGroup.addParticipant(participantId);
        expenseGroup.activate();

        assertThat(expenseGroup.getParticipants().size())
                .isEqualTo(2);

        assertThat(expenseGroup.isActivated())
                .isTrue();
    }
}
