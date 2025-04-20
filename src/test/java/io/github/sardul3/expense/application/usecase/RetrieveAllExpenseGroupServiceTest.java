package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.dto.RetrieveExpenseGroupsResponse;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RetrieveAllExpenseGroupServiceTest {

    private ExpenseGroupRepository expenseGroupRepository;
    private RetrieveAllExpenseGroupService expenseGroupService;

    @BeforeEach
    void setUp() {
        expenseGroupRepository = mock(ExpenseGroupRepository.class);
        expenseGroupService = new RetrieveAllExpenseGroupService(expenseGroupRepository);
    }

    @Nested
    @DisplayName("Retrieve All Expense Groups Use Case")
    class RetrieveAllExpenseGroupsTests {

        @Test
        @DisplayName("should return all expense groups when data exists")
        void shouldReturnExpenseGroupsWhenDataExists() {
            // Arrange
            ExpenseGroup group = ExpenseGroup.from(GroupName.withName("Test Group"), Participant.withEmail("owner@test.com"));
            when(expenseGroupRepository.findAll()).thenReturn(List.of(group));

            // Act
            List<RetrieveExpenseGroupsResponse> response = expenseGroupService.getAllExpenseGroups();

            // Assert
            assertThat(response)
                    .hasSize(1)
                    .extracting(RetrieveExpenseGroupsResponse::name)
                    .containsExactly("Test Group");
        }

        @Test
        @DisplayName("should return empty list when no expense groups exist")
        void shouldReturnEmptyListWhenNoGroupsExist() {
            // Arrange
            when(expenseGroupRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<RetrieveExpenseGroupsResponse> response = expenseGroupService.getAllExpenseGroups();

            // Assert
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("should gracefully handle groups with unexpected null names")
        void shouldHandleGroupWithNullName() {
            // Arrange
            ExpenseGroup invalidGroup = mock(ExpenseGroup.class);
            when(invalidGroup.getGroupName()).thenReturn(null);

            ExpenseGroup validGroup = mock(ExpenseGroup.class);
            when(validGroup.getGroupName()).thenReturn(GroupName.withName("Test Group"));

            when(expenseGroupRepository.findAll()).thenReturn(List.of(invalidGroup, validGroup));

            // Act
            List<RetrieveExpenseGroupsResponse> response = expenseGroupService.getAllExpenseGroups();

            // Assert
            assertThat(response)
                    .hasSize(1)
                    .extracting(RetrieveExpenseGroupsResponse::name)
                    .containsExactly((String) "Test Group");
        }

        @Test
        @DisplayName("should handle multiple groups correctly")
        void shouldHandleMultipleGroupsCorrectly() {
            // Arrange
            ExpenseGroup group1 = ExpenseGroup.from(GroupName.withName("Group One"), Participant.withEmail("one@test.com"));
            ExpenseGroup group2 = ExpenseGroup.from(GroupName.withName("Group Two"), Participant.withEmail("two@test.com"));

            when(expenseGroupRepository.findAll()).thenReturn(List.of(group1, group2));

            // Act
            List<RetrieveExpenseGroupsResponse> response = expenseGroupService.getAllExpenseGroups();

            // Assert
            assertThat(response)
                    .hasSize(2)
                    .extracting(RetrieveExpenseGroupsResponse::name)
                    .containsExactlyInAnyOrder("Group One", "Group Two");
        }
    }
}
