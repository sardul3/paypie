package io.github.sardul3.expense.application.port;

import io.github.sardul3.expense.application.port.in.CreateExpenseGroupCommand;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;

public class CreateExpenseGroupService implements CreateExpenseGroupUseCase {

    private final ExpenseGroupRepository expenseGroupRepository;
    private final ExpenseGroupMapper expenseGroupMapper;

    public CreateExpenseGroupService(ExpenseGroupRepository expenseGroupRepository, ExpenseGroupMapper expenseGroupMapper) {
        this.expenseGroupRepository = expenseGroupRepository;
        this.expenseGroupMapper = expenseGroupMapper;
    }

    @Override
    public ExpenseGroup createExpenseGroup(CreateExpenseGroupCommand CreateExpenseGroupCommand) {
        if(expenseGroupRepository.existsByName(CreateExpenseGroupCommand.getName())) {
            throw new ExpenseGroupAlreadyExistsException("Expense group with name " + CreateExpenseGroupCommand.getName() + " already exists");
        }

        ExpenseGroup expenseGroup = expenseGroupMapper.toExpenseGroup(CreateExpenseGroupCommand);
        return expenseGroupRepository.save(expenseGroup);
    }
}
