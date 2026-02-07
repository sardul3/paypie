package io.github.sardul3.expense.adapter.in.web.config;

import io.github.sardul3.expense.application.port.in.AddParticipantUseCase;
import io.github.sardul3.expense.application.port.in.CreateExpenseActivityUseCase;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import io.github.sardul3.expense.application.port.in.GetGroupBalanceUseCase;
import io.github.sardul3.expense.application.port.in.RetrieveAllExpenseGroupsUseCase;
import io.github.sardul3.expense.application.port.in.RetrieveExpenseGroupUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.application.usecase.AddParticipantService;
import io.github.sardul3.expense.application.usecase.CreateExpenseActivityService;
import io.github.sardul3.expense.application.usecase.CreateExpenseGroupService;
import io.github.sardul3.expense.application.usecase.GetGroupBalanceService;
import io.github.sardul3.expense.application.usecase.RetrieveAllExpenseGroupService;
import io.github.sardul3.expense.application.usecase.RetrieveExpenseGroupService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanConfig {
    @Bean
    public CreateExpenseGroupUseCase createExpenseGroupUseCase(ExpenseGroupRepository expenseGroupRepository) {
        return new CreateExpenseGroupService(expenseGroupRepository);
    }

    @Bean
    public RetrieveAllExpenseGroupsUseCase retrieveAllExpenseGroupsUseCase(ExpenseGroupRepository expenseGroupRepository) {
        return new RetrieveAllExpenseGroupService(expenseGroupRepository);
    }

    @Bean
    public CreateExpenseActivityUseCase createExpenseActivityUseCase(ExpenseGroupRepository expenseGroupRepository) {
        return new CreateExpenseActivityService(expenseGroupRepository);
    }

    @Bean
    public RetrieveExpenseGroupUseCase retrieveExpenseGroupUseCase(ExpenseGroupRepository expenseGroupRepository) {
        return new RetrieveExpenseGroupService(expenseGroupRepository);
    }

    @Bean
    public AddParticipantUseCase addParticipantUseCase(ExpenseGroupRepository expenseGroupRepository) {
        return new AddParticipantService(expenseGroupRepository);
    }

    @Bean
    public GetGroupBalanceUseCase getGroupBalanceUseCase(ExpenseGroupRepository expenseGroupRepository) {
        return new GetGroupBalanceService(expenseGroupRepository);
    }
}
