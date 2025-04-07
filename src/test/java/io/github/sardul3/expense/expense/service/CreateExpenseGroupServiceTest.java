package io.github.sardul3.expense.expense.service;

import io.github.sardul3.expense.adapter.out.persistence.InMemoryExpenseGroupRepository;
import io.github.sardul3.expense.application.port.CreateExpenseGroupService;
import io.github.sardul3.expense.application.port.ExpenseGroupAlreadyExistsException;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupCommand;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupResponse;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExpenseGroup | Domain Service Behavior")
public class CreateExpenseGroupServiceTest {

    private ExpenseGroupRepository expenseGroupRepository;
    private CreateExpenseGroupService createExpenseGroupService;

    @BeforeEach
    void setUp() {
        this.expenseGroupRepository = new InMemoryExpenseGroupRepository();
        this.createExpenseGroupService =
                new CreateExpenseGroupService(expenseGroupRepository);
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

    @Nested
    class CreateExpenseGroupServiceExceptionPropagationTest {
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
        @DisplayName("Should throw IllegalArgumentException for null group name")
        void shouldThrowForNullGroupName() {
            CreateExpenseGroupCommand command = new CreateExpenseGroupCommand(null, "user@example.com");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    createExpenseGroupService.createExpenseGroup(command));

            assertEquals("Name cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for empty group name")
        void shouldThrowForEmptyGroupName() {
            CreateExpenseGroupCommand command = new CreateExpenseGroupCommand("   ", "user@example.com");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    createExpenseGroupService.createExpenseGroup(command));

            assertEquals("Name cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for group name exceeding max length")
        void shouldThrowForOverlyLongGroupName() {
            String longName = "a".repeat(51);
            CreateExpenseGroupCommand command = new CreateExpenseGroupCommand(longName, "user@example.com");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    createExpenseGroupService.createExpenseGroup(command));

            assertTrue(exception.getMessage().contains("Name cannot be longer than"));
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for null participant email")
        void shouldThrowForNullEmail() {
            CreateExpenseGroupCommand command = new CreateExpenseGroupCommand("Test Group", null);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    createExpenseGroupService.createExpenseGroup(command));

            assertEquals("Email cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid email")
        void shouldThrowForInvalidEmail() {
            CreateExpenseGroupCommand command = new CreateExpenseGroupCommand("Test Group", "not-a-valid-email");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    createExpenseGroupService.createExpenseGroup(command));

            assertEquals("Email is not valid - Invalid email format", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw custom exception if group name already exists")
        void shouldThrowForDuplicateGroupName() {
            CreateExpenseGroupCommand command = new CreateExpenseGroupCommand("MyGroup", "user@abc.com");

            // first call succeeds
            createExpenseGroupService.createExpenseGroup(command);

            // second call with same name throws
            assertThrows(ExpenseGroupAlreadyExistsException.class, () -> createExpenseGroupService.createExpenseGroup(command));
        }
    }

}
