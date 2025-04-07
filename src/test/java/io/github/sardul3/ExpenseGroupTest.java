package io.github.sardul3;

import io.github.sardul3.account.domain.ExpenseGroup;
import io.github.sardul3.account.domain.GroupName;
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
}
