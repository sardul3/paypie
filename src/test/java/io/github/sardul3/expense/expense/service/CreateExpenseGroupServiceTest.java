package io.github.sardul3.expense.expense.service;

import io.github.sardul3.expense.adapter.out.persistence.InMemoryExpenseGroupRepository;
import io.github.sardul3.expense.application.port.CreateExpenseGroupService;
import io.github.sardul3.expense.application.port.ExpenseGroupAlreadyExistsException;
import io.github.sardul3.expense.application.port.ExpenseGroupMapper;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupCommand;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupResponse;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ExpenseGroup | Domain Service Behavior")
public class CreateExpenseGroupServiceTest {

    private ExpenseGroupRepository expenseGroupRepository;
    private ExpenseGroupMapper expenseGroupMapper;
    private CreateExpenseGroupService createExpenseGroupService;

    @BeforeEach
    void setUp() {
        this.expenseGroupRepository = new InMemoryExpenseGroupRepository();
        this.expenseGroupMapper = new ExpenseGroupMapper();
        this.createExpenseGroupService =
                new CreateExpenseGroupService(expenseGroupRepository, expenseGroupMapper);
    }


    @Test
    @DisplayName("CreateExpenseGroupService | should create a group with all right input")
    public void testCreateExpenseGroup() {
        CreateExpenseGroupCommand createExpenseGroupCommand = new CreateExpenseGroupCommand(
                "demo", "user@company.com"
        );

        CreateExpenseGroupResponse expenseGroup = createExpenseGroupService.createExpenseGroup(createExpenseGroupCommand);

        assertThat(expenseGroup)
                .isNotNull();
        assertThat(expenseGroup.name()).isEqualTo("demo");
    }

    @Test
    @DisplayName("CreateExpenseGroupService | should throw custom exception for duplicate groupname")
    public void testCreateExpenseGroupWithDuplicateName() {
        CreateExpenseGroupCommand createExpenseGroupCommand = new CreateExpenseGroupCommand(
                "demo", "user@company.com"
        );

        createExpenseGroupService.createExpenseGroup(createExpenseGroupCommand);

        assertThrows(ExpenseGroupAlreadyExistsException.class, () -> createExpenseGroupService.createExpenseGroup(createExpenseGroupCommand));
    }

    @Test
    @DisplayName("CreateExpenseGroupService | usecase should not leak internal domain model to external layers")
    void testCreateExpenseGroupShouldReturnCustomDto() {
        CreateExpenseGroupCommand command = new CreateExpenseGroupCommand(
                "demo", "user@company.com"
        );

        CreateExpenseGroupResponse response = createExpenseGroupService.createExpenseGroup(command);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("demo");
        assertThat(response.id().toString()).isNotBlank();
    }
}
