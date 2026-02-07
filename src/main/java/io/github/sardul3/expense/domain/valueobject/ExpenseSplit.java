package io.github.sardul3.expense.domain.valueobject;

import io.github.sardul3.expense.domain.common.annotation.ValueObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueObject(
        description = "Represents a manner to split the expenses between group participants",
        boundedContext = "expense-management"
)
public class ExpenseSplit {
    private final boolean splitEvenlyForAllMembers;
    private final List<ParticipantId> splitMembers;

    public ExpenseSplit(boolean splitEvenlyForAllMembers) {
        this.splitEvenlyForAllMembers = splitEvenlyForAllMembers;
        this.splitMembers = List.of();
    }

    private ExpenseSplit(boolean splitEvenlyForAllMembers, List<ParticipantId> splitMembers) {
        this.splitEvenlyForAllMembers = splitEvenlyForAllMembers;
        this.splitMembers = Collections.unmodifiableList(new ArrayList<>(splitMembers));
    }

    /**
     * Creates a custom split with the given members (copy preserved; input not mutated).
     */
    public static ExpenseSplit customSplit(List<ParticipantId> splitMembers) {
        return new ExpenseSplit(false, splitMembers != null ? splitMembers : List.of());
    }

    /**
     * Creates a custom split including the payer in the split members. Does not mutate the input list.
     */
    public static ExpenseSplit customSplitWithPayerIncluded(List<ParticipantId> participantIds, ParticipantId payer) {
        List<ParticipantId> copy = new ArrayList<>(participantIds != null ? participantIds : List.of());
        if (!copy.contains(payer)) {
            copy.add(payer);
        }
        return new ExpenseSplit(false, copy);
    }

    public boolean isSplitEvenlyForAllMembers() {
        return splitEvenlyForAllMembers;
    }

    public List<ParticipantId> getSplitMembers() {
        return splitMembers;
    }
}
