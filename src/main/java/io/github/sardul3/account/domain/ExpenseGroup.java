package io.github.sardul3.account.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        this.participants = new ArrayList<ParticipantId>();
        this.participants.add(groupCreator.getParticipantId());
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
