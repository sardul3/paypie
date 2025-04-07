package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.dto.CreateExpenseGroupCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupAlreadyExistsException;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;

public class CreateExpenseGroupService implements CreateExpenseGroupUseCase {

    private final ExpenseGroupRepository expenseGroupRepository;

    public CreateExpenseGroupService(ExpenseGroupRepository expenseGroupRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
    }

    @Override
    public CreateExpenseGroupResponse createExpenseGroup(CreateExpenseGroupCommand command) {
        GroupName groupName = GroupName.withName(command.getName());

        if (expenseGroupRepository.existsByName(groupName)) {
            throw new ExpenseGroupAlreadyExistsException("Expense group with name " + groupName.getName() + " already exists");
        }

        Participant creator = Participant.withEmail(command.getCreatedBy());
        ExpenseGroup group = ExpenseGroup.from(groupName, creator);
        expenseGroupRepository.save(group);

        return new CreateExpenseGroupResponse(group.getId(), group.getGroupName());
    }
}
