package io.github.sardul3;

import io.github.sardul3.account.domain.ExpenseGroup;
import io.github.sardul3.account.domain.GroupName;
import io.github.sardul3.account.domain.Participant;
import io.github.sardul3.account.domain.ParticipantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
}
