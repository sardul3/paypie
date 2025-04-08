package io.github.sardul3.expense.domain.model;

import io.github.sardul3.expense.domain.common.annotation.AggregateRoot;
import io.github.sardul3.expense.domain.common.base.BaseAggregateRoot;
import io.github.sardul3.expense.domain.valueobject.ExpenseGroupId;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import io.github.sardul3.expense.domain.valueobject.ParticipantId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AggregateRoot(
        description = "Represents a group of participants collaborating on shared expenses",
        boundedContext = "expense-management"
)
public class ExpenseGroup extends BaseAggregateRoot<ExpenseGroupId> {

    private static final int MIN_MEMBERS_NEEDED_BEFORE_ACTIVATION = 2;

    private final GroupName groupName;
    private final Participant groupCreator;
    private boolean isActivated = false;
    private List<ParticipantId> participants;

    private ExpenseGroup(ExpenseGroupId expenseGroupId , GroupName groupName, Participant groupCreator) {
        super(expenseGroupId);
        this.groupName = groupName;
        this.groupCreator = groupCreator;
        this.participants = new ArrayList<>();
        this.participants.add(groupCreator.getParticipantId());
    }

    public static ExpenseGroup from(GroupName groupName, Participant creator) {
        return new ExpenseGroup(
                ExpenseGroupId.generate(),
                groupName,
                creator
        );
    }

    public GroupName getGroupName() {
        return groupName;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public Participant getGroupCreator() {
        return groupCreator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExpenseGroup that = (ExpenseGroup) o;
        return isActivated == that.isActivated && Objects.equals(groupName, that.groupName) && Objects.equals(groupCreator, that.groupCreator) && Objects.equals(participants, that.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), groupName, groupCreator, isActivated, participants);
    }

    public void addParticipant(ParticipantId participantId) {
        if(this.participants.contains(participantId)) {
            throw new IllegalArgumentException("Participant " + participantId + " already exists in the expense group");
        }
        this.participants.add(participantId);
    }

    public List<ParticipantId> getParticipants() {
        return participants;
    }

    public void activate() {
        if(this.participants.size() < MIN_MEMBERS_NEEDED_BEFORE_ACTIVATION) {
            throw new IllegalStateException("Group cannot be activated as it has less than " + MIN_MEMBERS_NEEDED_BEFORE_ACTIVATION + " members");
        }
        this.isActivated = true;
    }
}
