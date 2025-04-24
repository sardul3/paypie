package io.github.sardul3.expense.domain.model;

import io.github.sardul3.expense.domain.common.annotation.AggregateRoot;
import io.github.sardul3.expense.domain.common.base.BaseAggregateRoot;
import io.github.sardul3.expense.domain.valueobject.ExpenseGroupId;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import io.github.sardul3.expense.domain.valueobject.Money;
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
    private List<Participant> participants;
    private List<ExpenseActivity> activities;

    private ExpenseGroup(ExpenseGroupId expenseGroupId , GroupName groupName, Participant groupCreator) {
        super(expenseGroupId);
        this.groupName = groupName;
        this.groupCreator = groupCreator;
        this.participants = new ArrayList<>();
        this.participants.add(groupCreator);
        this.activities = new ArrayList<>();
    }

    public static ExpenseGroup from(GroupName groupName, Participant creator) {
        return new ExpenseGroup(
                ExpenseGroupId.generate(),
                groupName,
                creator
        );
    }

    public void addParticipant(Participant participant) {
        if(this.participants.contains(participant)) {
            throw new IllegalArgumentException("Participant " + participant + " already exists in the expense group");
        }
        this.participants.add(participant);
    }

    public void activate() {
        if(this.participants.size() < MIN_MEMBERS_NEEDED_BEFORE_ACTIVATION) {
            throw new IllegalStateException("Group cannot be activated as it has less than " + MIN_MEMBERS_NEEDED_BEFORE_ACTIVATION + " members");
        }
        this.isActivated = true;
    }

    public void addActivity(ExpenseActivity expenseActivity) {
        validateActivity(expenseActivity);
        this.activities.add(expenseActivity);
        calculateGroupBalance(expenseActivity);
    }

    public List<ExpenseActivity> getActivities() {
        return activities;
    }

    public List<Participant> getParticipants() {
        return participants;
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

    private void validateActivity(ExpenseActivity expenseActivity) {
        if(!this.isActivated) {
            throw new IllegalStateException("Activity cannot be registered as it has not been activated");
        }
        Participant activityCreatedBy = expenseActivity.getPaidBy();
        if(!this.participants.contains(activityCreatedBy)) {
            throw new IllegalArgumentException("Participant " + activityCreatedBy + " does not exist in the expense group");
        }
        if(this.activities.contains(expenseActivity)) {
            throw new IllegalArgumentException("Activity " + expenseActivity + " already exists in the expense group");
        }
    }

    private void calculateGroupBalance(ExpenseActivity activity) {
        List<Participant> splitMembers = resolveSplitMembers(activity);
        Money total = activity.getAmount();
        int count = splitMembers.size();
        Money share = total.split(count);

        for (Participant member : splitMembers) {
            if (!member.getParticipantId().equals(activity.getPaidBy().getParticipantId())) {
                member.debit(share);
            }
        }
        Participant payer = activity.getPaidBy();
        payer.credit(total.subtract(share));
    }

    private List<Participant> resolveSplitMembers(ExpenseActivity activity) {
        if (activity.getSplit().isSplitEvenlyForAllMembers()) {
            return new ArrayList<>(participants);
        }
        List<ParticipantId> splitMembers = new ArrayList<>(activity.getSplit().getSplitMembers());
        splitMembers.add(activity.getPaidBy().getParticipantId());
        return participants.stream()
                .filter(p -> splitMembers.contains(p.getParticipantId()))
                .toList();
    }

}
