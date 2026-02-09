package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.dto.ActivityView;
import io.github.sardul3.expense.application.dto.ExpenseHistoryPageResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.out.ExpenseActivityQueryRepository;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetExpenseHistoryServiceTest {

    private ExpenseGroupRepository expenseGroupRepository;
    private ExpenseActivityQueryRepository expenseActivityQueryRepository;
    private GetExpenseHistoryService getExpenseHistoryService;

    @BeforeEach
    void setUp() {
        expenseGroupRepository = mock(ExpenseGroupRepository.class);
        expenseActivityQueryRepository = mock(ExpenseActivityQueryRepository.class);
        getExpenseHistoryService = new GetExpenseHistoryService(expenseGroupRepository, expenseActivityQueryRepository);
    }

    @Test
    @DisplayName("should return paginated history when group exists")
    void shouldReturnPaginatedHistoryWhenGroupExists() {
        UUID groupId = UUID.randomUUID();
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), Participant.withEmail("a@b.com"));
        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        ExpenseHistoryPageResponse pageResponse = new ExpenseHistoryPageResponse(
                List.of(new ActivityView(UUID.randomUUID(), "Dinner", BigDecimal.valueOf(50), UUID.randomUUID(), true)),
                1L, 1, 0, 20
        );
        when(expenseActivityQueryRepository.findByGroupId(groupId, 0, 20)).thenReturn(pageResponse);

        ExpenseHistoryPageResponse response = getExpenseHistoryService.getExpenseHistory(groupId, 0, 20);

        assertThat(response.content()).hasSize(1);
        assertThat(response.content().get(0).description()).isEqualTo("Dinner");
        assertThat(response.totalElements()).isEqualTo(1);
        assertThat(response.number()).isZero();
        assertThat(response.size()).isEqualTo(20);
    }

    @Test
    @DisplayName("should throw ExpenseGroupNotFoundException when group not found")
    void shouldThrowWhenGroupNotFound() {
        UUID groupId = UUID.randomUUID();
        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getExpenseHistoryService.getExpenseHistory(groupId, 0, 20))
                .isInstanceOf(ExpenseGroupNotFoundException.class)
                .hasMessageContaining("Expense group not found");
    }

    @Nested
    @DisplayName("GetExpenseHistory use case | Edge cases and validation")
    class EdgeCasesAndValidation {

        @Test
        @DisplayName("When page is negative, throw IllegalArgumentException")
        void whenPageIsNegative_throwIllegalArgumentException() {
            UUID groupId = UUID.randomUUID();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(ExpenseGroup.from(GroupName.withName("g"), Participant.withEmail("a@b.com"))));

            assertThatThrownBy(() -> getExpenseHistoryService.getExpenseHistory(groupId, -1, 20))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("page");
        }

        @Test
        @DisplayName("When size is zero, throw IllegalArgumentException")
        void whenSizeIsZero_throwIllegalArgumentException() {
            UUID groupId = UUID.randomUUID();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(ExpenseGroup.from(GroupName.withName("g"), Participant.withEmail("a@b.com"))));

            assertThatThrownBy(() -> getExpenseHistoryService.getExpenseHistory(groupId, 0, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("size");
        }

        @Test
        @DisplayName("When size is negative, throw IllegalArgumentException")
        void whenSizeIsNegative_throwIllegalArgumentException() {
            UUID groupId = UUID.randomUUID();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(ExpenseGroup.from(GroupName.withName("g"), Participant.withEmail("a@b.com"))));

            assertThatThrownBy(() -> getExpenseHistoryService.getExpenseHistory(groupId, 0, -5))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("When size exceeds maximum allowed, throw IllegalArgumentException")
        void whenSizeExceedsMaximum_throwIllegalArgumentException() {
            UUID groupId = UUID.randomUUID();
            when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(ExpenseGroup.from(GroupName.withName("g"), Participant.withEmail("a@b.com"))));

            assertThatThrownBy(() -> getExpenseHistoryService.getExpenseHistory(groupId, 0, 10_000))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("size");
        }

        @Test
        @DisplayName("When groupId is null, throw rather than proceed")
        void whenGroupIdIsNull_throwRatherThanProceed() {
            assertThatThrownBy(() -> getExpenseHistoryService.getExpenseHistory(null, 0, 20))
                    .satisfies(t -> assertThat(t).isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class));
        }
    }
}
