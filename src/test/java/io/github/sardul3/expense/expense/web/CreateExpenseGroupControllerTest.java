package io.github.sardul3.expense.expense.web;


import io.github.sardul3.expense.adapter.in.web.CreateExpenseGroupController;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateExpenseGroupControllerTest {

    @Mock
    private CreateExpenseGroupUseCase createExpenseGroupUseCase;

    @InjectMocks
    private CreateExpenseGroupController createExpenseGroupController;

    @Test
    void shouldReturn201CreatedHttpResponseEntityForHappyPath() {
        ExpenseGroup expenseGroup = ExpenseGroup.from(
                GroupName.withName("demo"),
                Participant.withEmail("user@demo.com")
        );
        ResponseEntity<String> response = createExpenseGroupController.createExpenseGroup(expenseGroup);

        assertNotNull(response);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
        verify(createExpenseGroupUseCase, times(1)).createExpenseGroup(any());
    }
}
