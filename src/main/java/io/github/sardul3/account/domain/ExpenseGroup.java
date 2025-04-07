package io.github.sardul3.account.domain;

public class ExpenseGroup extends BaseAggregateRoot<ExpenseGroupId> {
    private final GroupName groupName;
    private final Participant groupCreator;
    private boolean isActivated = false;

    private ExpenseGroup(ExpenseGroupId expenseGroupId , GroupName groupName, Participant groupCreator) {
        super(expenseGroupId);
        this.groupName = groupName;
        this.groupCreator = groupCreator;
    }

    public static ExpenseGroup from(String groupName, Participant creator) {
        GroupName group = GroupName.withName(groupName);
        return new ExpenseGroup(
                ExpenseGroupId.generate(),
                group,
                creator
        );
    }

    public GroupName getGroupName() {
        return groupName;
    }

    public boolean isActivated() {
        return isActivated;
    }
}
