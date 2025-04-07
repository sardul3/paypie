package io.github.sardul3.account.domain;

public class ExpenseGroup {
    private final GroupName groupName;

    public ExpenseGroup(GroupName groupName) {
        this.groupName = groupName;
    }

    public GroupName getGroupName() {
        return groupName;
    }
}
