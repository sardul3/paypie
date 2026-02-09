package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.common.annotation.UseCase;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.exception.ParticipantNotFoundInGroupException;
import io.github.sardul3.expense.application.port.in.CreateExpenseActivityUseCase;
import io.github.sardul3.expense.application.port.out.DomainEventPublisher;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.event.ExpenseAddedEvent;
import io.github.sardul3.expense.domain.model.ExpenseActivity;
import io.github.sardul3.expense.domain.valueobject.ExpenseSplit;
import io.github.sardul3.expense.domain.valueobject.Money;
import io.github.sardul3.expense.domain.valueobject.ParticipantId;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Use case: creates an expense activity in a group, applies split logic, and updates balances.
 */
@UseCase(description = "Creates a new expense activity within the group", inputPort = CreateExpenseActivityUseCase.class)
public class CreateExpenseActivityService implements CreateExpenseActivityUseCase {

    private final ExpenseGroupRepository expenseGroupRepository;
    private final DomainEventPublisher domainEventPublisher;

    public CreateExpenseActivityService(ExpenseGroupRepository expenseGroupRepository,
                                        DomainEventPublisher domainEventPublisher) {
        this.expenseGroupRepository = expenseGroupRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public CreateExpenseActivityResponse createExpenseActivity(CreateExpenseActivityCommand command) {
        Objects.requireNonNull(command, "CreateExpenseActivityCommand cannot be null");
        var groupId = command.groupId();
        var expenseGroup = expenseGroupRepository.findById(groupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException("Expense group not found: " + groupId));

        var paidBy = expenseGroup.getParticipantById(ParticipantId.from(command.paidBy()))
                .orElseThrow(() -> new ParticipantNotFoundInGroupException("Participant not found"));

        ExpenseActivity activity;
        if (command.splitWith() != null && !command.splitWith().isEmpty()) {
            List<ParticipantId> customSplit = command.splitWith().stream()
                    .map(ParticipantId::from)
                    .collect(Collectors.toList());
            for (ParticipantId splitId : customSplit) {
                if (expenseGroup.getParticipantById(splitId).isEmpty()) {
                    throw new IllegalArgumentException(
                            "Split participant " + splitId.getId() + " is not a member of the group");
                }
            }
            ExpenseSplit split = ExpenseSplit.customSplitWithPayerIncluded(customSplit, paidBy.getParticipantId());
            activity = ExpenseActivity.from(command.description(), Money.of(command.amount()), paidBy, split);
        } else {
            activity = ExpenseActivity.from(command.description(), Money.of(command.amount()), paidBy);
        }

        expenseGroup.addActivity(activity);
        expenseGroupRepository.save(expenseGroup);

        domainEventPublisher.publish(new ExpenseAddedEvent(
                groupId,
                activity.getDescription(),
                activity.getAmount().getAmount(),
                paidBy.getParticipantId().getId()));

        return new CreateExpenseActivityResponse(
                activity.getDescription(),
                activity.getAmount().toString(),
                paidBy.getBalance()
        );
    }
}
