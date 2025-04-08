package io.github.sardul3.expense.expense.web;


import io.github.sardul3.expense.adapter.in.web.CreateExpenseGroupController;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CreateExpenseGroupControllerTest {

    @Mock
    private CreateExpenseGroupUseCase createExpenseGroupUseCase;

    @InjectMocks
    private CreateExpenseGroupController createExpenseGroupController;

    @Test
    void shouldReturn201CreatedHttpResponseEntityForHappyPath() {
        CreateExpenseGroupController controller = new CreateExpenseGroupController();

        ExpenseGroup expenseGroup = ExpenseGroup.from(
                GroupName.withName("demo"),
                Participant.withEmail("user@demo.com")
        );
        ResponseEntity<String> response = controller.createExpenseGroup(expenseGroup);

        assertNotNull(response);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
        verify(createExpenseGroupUseCase.createExpenseGroup(any()), times(1));
    }
}
