package io.github.sardul3.expense.domain.valueobject;

import io.github.sardul3.expense.domain.common.annotation.ValueObject;

import java.util.List;

@ValueObject(
        description = "Represents a manner to split the expenses between group participants",
        boundedContext = "expense-management"
)
public class ExpenseSplit {
    private  boolean splitEvenlyForAllMembers;
    private List<ParticipantId> splitMembers;

    public ExpenseSplit(boolean splitEvenlyForAllMembers) {
        this.splitEvenlyForAllMembers = splitEvenlyForAllMembers;
    }

    public boolean isSplitEvenlyForAllMembers() {
        return splitEvenlyForAllMembers;
    }

    public List<ParticipantId> getSplitMembers() {
        return splitMembers;
    }

    public void setSplitMembers(List<ParticipantId> splitMembers) {
        this.splitMembers = splitMembers;
    }
}
