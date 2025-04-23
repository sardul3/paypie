package io.github.sardul3.expense.expense.model;

import io.github.sardul3.expense.domain.model.ExpenseActivity;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import io.github.sardul3.expense.domain.valueobject.Money;
import io.github.sardul3.expense.domain.valueobject.ParticipantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ExpenseGroup | Aggregate Root Behavior")
public class ExpenseGroupTest {

    @Test
    @DisplayName("Expense Group | should create a valid expense group")
    void expenseGroupShouldBeCreatedWithCorrectName() {
        GroupName groupName = GroupName.withName("demo");
        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName,
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
                groupName, createdBy
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
                groupName, createdBy
        );

        ParticipantId participantId = participant.getId();
        expenseGroup.addParticipant(participant);

        assertThat(expenseGroup.getParticipants().size())
                .isEqualTo(2);
    }

    @Test
    @DisplayName("Expense Group | should add group creator as a participant by default")
    void expenseGroupShouldAddCreatorAsParticipantByDefault() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("user@example.com");

        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName, createdBy
        );

        assertThat(expenseGroup.getParticipants().size())
                .isEqualTo(1);

        assertThat(expenseGroup.getParticipants().get(0).getId().getId())
                .isEqualTo(createdBy.getParticipantId().getId());
    }

    @Test
    @DisplayName("Expense Group | should have unique participants per group including creator")
    void expenseGroupShouldNotAddDuplicateParticipants() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("creator@example.com");
        Participant participant = Participant.withEmail("user@example.com");

        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName, createdBy
        );

        ParticipantId creatorId = createdBy.getId();
        assertThrows(IllegalArgumentException.class, () -> expenseGroup.addParticipant(createdBy));
    }

    @Test
    @DisplayName("Expense Group | should have unique participants per group")
    void expenseGroupShouldNotAddDuplicateParticipantsMultipleParticipants() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("creator@example.com");
        Participant participant = Participant.withEmail("user@example.com");
        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName, createdBy
        );

        ParticipantId participantId = participant.getId();
        expenseGroup.addParticipant(participant);
        assertThrows(IllegalArgumentException.class, () -> expenseGroup.addParticipant(participant));
    }

    @Test
    @DisplayName("Expense Group | cannot be activated if it does not have at least 2 members")
    void expenseGroupCannotBeActivatedBeforeTwoTotalMembers() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("user@example.com");

        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName, createdBy
        );

        assertThrows(IllegalStateException.class, () -> expenseGroup.activate());
    }

    @Test
    @DisplayName("Expense Group | can be activated if it has at least 2 members")
    void expenseGroupCanBeActivatedAfterTwoTotalMembers() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("user@example.com");

        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName, createdBy
        );

        Participant participant = Participant.withEmail("user@example.com");
        ParticipantId participantId = participant.getId();
        expenseGroup.addParticipant(participant);
        expenseGroup.activate();

        assertThat(expenseGroup.getParticipants().size())
                .isEqualTo(2);

        assertThat(expenseGroup.isActivated())
                .isTrue();
    }

    @Test
    void expenseGroupAreComparedById() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("user@example.com");
        ExpenseGroup expenseGroup = ExpenseGroup.from(groupName, createdBy);

        ExpenseGroup expenseGroup2 = ExpenseGroup.from(groupName, createdBy);

        assertNotEquals(expenseGroup, expenseGroup2);
    }

    @Test
    void expenseGroupShouldHaveGroupCreatorAvailable() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("user@example.com");
        ExpenseGroup expenseGroup = ExpenseGroup.from(groupName, createdBy);

        assertThat(expenseGroup.getGroupCreator()).isNotNull();
        assertThat(expenseGroup.getGroupCreator().getEmail()).isEqualTo("user@example.com");
    }

    @Test
    void expenseGroupShouldRejectActivitiesForAnInactiveGroup() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("user@example.com");
        ExpenseGroup expenseGroup = ExpenseGroup.from(groupName, createdBy);

        String activityDescription = "This is a test activity";
        Money activityAmount = Money.of(BigDecimal.TEN);
        Participant paidBy = Participant.withEmail("user@example.com");
        ExpenseActivity activity = ExpenseActivity.from(activityDescription, activityAmount, paidBy);

        assertThatThrownBy(() -> expenseGroup.addActivity(activity))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void expenseGroupShouldAcceptActivitesFromRegisteredMembers() {
        String activityDescription = "This is a test activity";
        Money activityAmount = Money.of(BigDecimal.TEN);
        Participant paidBy = Participant.withEmail("user@example.com");
        ParticipantId paidById = paidBy.getParticipantId();
        ExpenseActivity activity = ExpenseActivity.from(activityDescription, activityAmount, paidBy);

        GroupName groupName = GroupName.withName("demo");
        ExpenseGroup expenseGroup = ExpenseGroup.from(groupName, paidBy);

        Participant anotherParticipant = Participant.withEmail("another@example.com");
        expenseGroup.addParticipant(anotherParticipant);

        expenseGroup.addActivity(activity);

        assertThat(expenseGroup.getActivities())
                .hasSize(1);

    }

    @Test
    void expenseGroupShouldRejectActivitesFromUnknownMembers() {
        String activityDescription = "This is a test activity";
        Money activityAmount = Money.of(BigDecimal.TEN);
        Participant paidBy = Participant.withEmail("user@example.com");

        GroupName groupName = GroupName.withName("demo");
        ExpenseGroup expenseGroup = ExpenseGroup.from(groupName, paidBy);

        Participant anotherParticipant = Participant.withEmail("another@example.com");
        expenseGroup.addParticipant(anotherParticipant);

        Participant unknownParticipant = Participant.withEmail("unknown@example.com");
        ExpenseActivity activity = ExpenseActivity.from(activityDescription, activityAmount, unknownParticipant);

        assertThatThrownBy(() -> expenseGroup.addActivity(activity))
        .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not exist in the expense group");
    }

    @Test
    void expenseGroupShouldRejectDuplicateActivities() {
        String activityDescription = "This is a test activity";
        Money activityAmount = Money.of(BigDecimal.TEN);
        Participant paidBy = Participant.withEmail("user@example.com");

        GroupName groupName = GroupName.withName("demo");
        ExpenseGroup expenseGroup = ExpenseGroup.from(groupName, paidBy);

        Participant anotherParticipant = Participant.withEmail("another@example.com");
        expenseGroup.addParticipant(anotherParticipant);

        ExpenseActivity activity = ExpenseActivity.from(activityDescription, activityAmount, anotherParticipant);

        expenseGroup.addActivity(activity);

        assertThatThrownBy(() -> expenseGroup.addActivity(activity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists in the expense group");
    }
}
