package io.github.sardul3.expense.expense.service;

import io.github.sardul3.expense.domain.model.ExpenseGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ExpenseGroup | Domain Service Behavior")
public class CreateExpenseGroupServiceTest {

    @Test
    public void testCreateExpenseGroup() {
        CreateExpenseGroupService service = new CreateExpenseGroupService();

        CreateExpenseGroupCommand createExpenseGroupCommand = new CreateExpenseGroupCommand();
        ExpenseGroup expenseGroup = service.createExpense(createExpenseGroupCommand);

        assertThat(expenseGroup)
                .isNotNull();
    }
}
