package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.dto.ExpenseGroupDetailResponse;
import io.github.sardul3.expense.application.dto.ParticipantBalanceView;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RetrieveExpenseGroupServiceTest {

    private ExpenseGroupRepository expenseGroupRepository;
    private RetrieveExpenseGroupService retrieveExpenseGroupService;

    @BeforeEach
    void setUp() {
        expenseGroupRepository = mock(ExpenseGroupRepository.class);
        retrieveExpenseGroupService = new RetrieveExpenseGroupService(expenseGroupRepository);
    }

    @Test
    @DisplayName("should return group detail when group exists")
    void shouldReturnGroupDetailWhenGroupExists() {
        Participant creator = Participant.withEmail("alice@example.com");
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("team-lunch"), creator);
        UUID groupId = group.getId().getId();

        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        ExpenseGroupDetailResponse response = retrieveExpenseGroupService.getExpenseGroup(groupId);

        assertThat(response.id()).isEqualTo(groupId);
        assertThat(response.name()).isEqualTo("team-lunch");
        assertThat(response.createdBy()).isEqualTo("alice@example.com");
        assertThat(response.activated()).isFalse();
        assertThat(response.participants())
                .hasSize(1)
                .first()
                .satisfies(p -> {
                    assertThat(p.email()).isEqualTo("alice@example.com");
                    assertThat(p.balance()).isEqualByComparingTo(BigDecimal.ZERO);
                });
    }

    @Test
    @DisplayName("should throw ExpenseGroupNotFoundException when group does not exist")
    void shouldThrowWhenGroupNotFound() {
        UUID groupId = UUID.randomUUID();
        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> retrieveExpenseGroupService.getExpenseGroup(groupId))
                .isInstanceOf(ExpenseGroupNotFoundException.class)
                .hasMessageContaining("Expense group not found");
    }

    @Test
    @DisplayName("should include multiple participants with balances")
    void shouldIncludeMultipleParticipantsWithBalances() {
        Participant creator = Participant.withEmail("alice@example.com");
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("trip"), creator);
        group.addParticipant(Participant.withEmail("bob@example.com"));
        UUID groupId = group.getId().getId();

        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        ExpenseGroupDetailResponse response = retrieveExpenseGroupService.getExpenseGroup(groupId);

        assertThat(response.participants()).hasSize(2);
        assertThat(response.participants().stream().map(ParticipantBalanceView::email).toList())
                .containsExactlyInAnyOrder("alice@example.com", "bob@example.com");
    }
}
