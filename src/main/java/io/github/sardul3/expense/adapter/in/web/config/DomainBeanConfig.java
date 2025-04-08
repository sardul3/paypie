package io.github.sardul3.expense.adapter.in.web.config;

import io.github.sardul3.expense.adapter.out.persistence.InMemoryExpenseGroupRepository;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.application.usecase.CreateExpenseGroupService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeanConfig {
    @Bean
    public CreateExpenseGroupUseCase createExpenseGroupUseCase(ExpenseGroupRepository expenseGroupRepository) {
        return new CreateExpenseGroupService(expenseGroupRepository);
    }

    @Bean
    public ExpenseGroupRepository expenseGroupRepository() {
        return new InMemoryExpenseGroupRepository();
    }
}
