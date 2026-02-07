package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.common.annotation.UseCase;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.exception.ParticipantNotFoundInGroupException;
import io.github.sardul3.expense.application.port.in.CreateExpenseActivityUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseActivity;
import io.github.sardul3.expense.domain.valueobject.ExpenseSplit;
import io.github.sardul3.expense.domain.valueobject.Money;
import io.github.sardul3.expense.domain.valueobject.ParticipantId;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use case: creates an expense activity in a group, applies split logic, and updates balances.
 */
@UseCase(description = "Creates a new expense activity within the group", inputPort = CreateExpenseActivityUseCase.class)
public class CreateExpenseActivityService implements CreateExpenseActivityUseCase {

    private final ExpenseGroupRepository expenseGroupRepository;

    public CreateExpenseActivityService(ExpenseGroupRepository expenseGroupRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
    }

    @Override
    public CreateExpenseActivityResponse createExpenseActivity(CreateExpenseActivityCommand command) {
        var expenseGroup = expenseGroupRepository.findById(command.groupId())
                .orElseThrow(() -> new ExpenseGroupNotFoundException("Expense Group not found"));

        var paidBy = expenseGroup.getParticipantById(ParticipantId.from( command.paidBy() ))
                .orElseThrow(() -> new ParticipantNotFoundInGroupException("Participant not found"));

        ExpenseActivity activity;
        if (command.splitWith() != null && !command.splitWith().isEmpty()) {
            List<ParticipantId> customSplit = command.splitWith().stream()
                    .map(ParticipantId::from)
                    .collect(Collectors.toList());
            ExpenseSplit split = ExpenseSplit.customSplitWithPayerIncluded(customSplit, paidBy.getParticipantId());
            activity = ExpenseActivity.from(command.description(), Money.of(command.amount()), paidBy, split);
        } else {
            activity = ExpenseActivity.from(command.description(), Money.of(command.amount()), paidBy);
        }

        expenseGroup.addActivity(activity);
        expenseGroupRepository.save(expenseGroup);

        return new CreateExpenseActivityResponse(
                activity.getDescription(),
                activity.getAmount().toString(),
                paidBy.getBalance()
        );
    }
}
