package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.dto.GroupBalanceResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetGroupBalanceServiceTest {

    private ExpenseGroupRepository expenseGroupRepository;
    private GetGroupBalanceService getGroupBalanceService;

    @BeforeEach
    void setUp() {
        expenseGroupRepository = mock(ExpenseGroupRepository.class);
        getGroupBalanceService = new GetGroupBalanceService(expenseGroupRepository);
    }

    @Test
    @DisplayName("should return balance when group exists")
    void shouldReturnBalanceWhenGroupExists() {
        Participant creator = Participant.withEmail("alice@example.com");
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), creator);
        group.addParticipant(Participant.withEmail("bob@example.com"));
        UUID groupId = group.getId().getId();

        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        GroupBalanceResponse response = getGroupBalanceService.getBalance(groupId);

        assertThat(response.groupId()).isEqualTo(groupId);
        assertThat(response.participants()).hasSize(2);
        assertThat(response.participants().stream().map(p -> p.email()).toList())
                .containsExactlyInAnyOrder("alice@example.com", "bob@example.com");
        assertThat(response.participants()).allMatch(p -> p.balance().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    @DisplayName("should throw ExpenseGroupNotFoundException when group does not exist")
    void shouldThrowWhenGroupNotFound() {
        UUID groupId = UUID.randomUUID();
        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getGroupBalanceService.getBalance(groupId))
                .isInstanceOf(ExpenseGroupNotFoundException.class)
                .hasMessageContaining("Expense group not found");
    }

    @Nested
    @DisplayName("GetGroupBalance use case | Edge cases and validation")
    class EdgeCasesAndValidation {

        @Test
        @DisplayName("When groupId is null, throw IllegalArgumentException or NPE rather than proceed")
        void whenGroupIdIsNull_throwRatherThanProceed() {
            assertThatThrownBy(() -> getGroupBalanceService.getBalance(null))
                    .satisfies(t -> assertThat(t).isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class));
        }
    }
}
