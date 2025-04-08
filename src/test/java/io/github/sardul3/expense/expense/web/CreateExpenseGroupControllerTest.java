package io.github.sardul3.expense.expense.web;


import io.github.sardul3.expense.adapter.in.web.CreateExpenseGroupController;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateExpenseGroupControllerTest {

    @Test
    void shouldReturnSimpleHttpResponseEntity() {
        CreateExpenseGroupController controller = new CreateExpenseGroupController();

        ExpenseGroup expenseGroup = ExpenseGroup.from(
                GroupName.withName("demo"),
                Participant.withEmail("user@demo.com")
        );
        ResponseEntity<String> response = controller.createExpenseGroup(expenseGroup);

        assertNotNull(response);
    }
}
