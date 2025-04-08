package io.github.sardul3.expense.adapter.in.web.config;

import io.github.sardul3.expense.adapter.out.persistence.inmemory.InMemoryExpenseGroupRepository;
import io.github.sardul3.expense.adapter.out.persistence.postgres.ExpenseGroupJpaRepository;
import io.github.sardul3.expense.adapter.out.persistence.postgres.PostgresExpenseGroupRepository;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.application.usecase.CreateExpenseGroupService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
public class DomainBeanConfig {
    @Bean
    public CreateExpenseGroupUseCase createExpenseGroupUseCase(ExpenseGroupRepository expenseGroupRepository) {
        return new CreateExpenseGroupService(expenseGroupRepository);
    }
}
