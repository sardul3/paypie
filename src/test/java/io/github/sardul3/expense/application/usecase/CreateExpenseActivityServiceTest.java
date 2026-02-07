package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.dto.CreateExpenseActivityCommand;
import io.github.sardul3.expense.application.dto.CreateExpenseActivityResponse;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateExpenseActivityServiceTest {

    private ExpenseGroupRepository expenseGroupRepository;
    private CreateExpenseActivityService service;

    ExpenseGroup expenseGroup;
    Participant groupCreator;
    Participant anotherParticipant;

    @BeforeEach
    void setUp() {
        expenseGroupRepository = mock(ExpenseGroupRepository.class);
        service = new CreateExpenseActivityService(expenseGroupRepository);

        groupCreator = Participant.withEmail("test@example.com");
        expenseGroup = ExpenseGroup.from(GroupName.withName("apt-group"), groupCreator);
         anotherParticipant = Participant.withEmail("test2@example.com");
        expenseGroup.addParticipant(anotherParticipant);

        expenseGroup.activate();
    }

    @Test
    void shouldCreateExpenseActivity() {
        when(expenseGroupRepository.findById(any())).thenReturn(Optional.ofNullable(expenseGroup));

        CreateExpenseActivityCommand createExpenseActivityCommand = new CreateExpenseActivityCommand(
            expenseGroup.getId().getId(),
                "food - doordash",
                BigDecimal.valueOf(150.00),
                groupCreator.getParticipantId().getId(),
                List.of(groupCreator.getParticipantId().getId(), anotherParticipant.getParticipantId().getId())
        );

        var expenseActivity =  service.createExpenseActivity(createExpenseActivityCommand);
        assertThat(expenseActivity)
                .extracting(CreateExpenseActivityResponse::payerBalance)
                .isEqualTo(BigDecimal.valueOf(7500, 2));

        assertThat(expenseGroup)
                .extracting(ExpenseGroup::getActivities)
                .asList()
                .hasSize(1);

        assertThat(anotherParticipant)
                .extracting(Participant::getBalance)
                .isEqualTo(BigDecimal.valueOf(-7500, 2));

    }

    @Test
    void shouldCreateExpenseActivityWithRightSplitForMultipleMembers() {
        Participant thirdParticipant = Participant.withEmail("test3@example.com");
        expenseGroup.addParticipant(thirdParticipant);

        when(expenseGroupRepository.findById(any())).thenReturn(Optional.ofNullable(expenseGroup));

        CreateExpenseActivityCommand createExpenseActivityCommand = new CreateExpenseActivityCommand(
                expenseGroup.getId().getId(),
                "food - doordash",
                BigDecimal.valueOf(300),
                groupCreator.getParticipantId().getId(),
                List.of(groupCreator.getParticipantId().getId(), anotherParticipant.getParticipantId().getId()
                , thirdParticipant.getParticipantId().getId())
        );

        var expenseActivity =  service.createExpenseActivity(createExpenseActivityCommand);
        assertThat(expenseActivity)
                .extracting(CreateExpenseActivityResponse::payerBalance)
                .isEqualTo(BigDecimal.valueOf(20000, 2));

        assertThat(expenseGroup)
                .extracting(ExpenseGroup::getActivities)
                .asList()
                .hasSize(1);

        assertThat(thirdParticipant)
                .extracting(Participant::getBalance)
                .isEqualTo(BigDecimal.valueOf(-10000, 2));

    }
}