package io.github.sardul3;

import io.github.sardul3.account.domain.ExpenseGroup;
import io.github.sardul3.account.domain.GroupName;
import io.github.sardul3.account.domain.Participant;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExpenseGroupTest {

    @Test
    void expenseGroupShouldBeCreatedWithCorrectName() {
        GroupName groupName = GroupName.withName("demo");
        ExpenseGroup expenseGroup = new ExpenseGroup(groupName);

        assertThat(expenseGroup.getGroupName())
                .isEqualTo(groupName);
    }

    void expenseGroupShouldHaveAtleastTwoMembers() {
        GroupName groupName = GroupName.withName("demo");
        Participant createdBy = Participant.withEmail("creator@example.com");
        Participant participant = Participant.withEmail("user@example.com");
        ExpenseGroup expenseGroup = ExpenseGroup.from(
                groupName, createdBy
        );

        assertThat(expenseGroup.isActivated())
                .isFalse();

    }
}
