package io.github.sardul3.expense.expense.web;

import io.github.sardul3.expense.adapter.in.web.controller.RetrieveExpenseGroupController;
import io.github.sardul3.expense.application.dto.ExpenseGroupDetailResponse;
import io.github.sardul3.expense.application.dto.ParticipantBalanceView;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.in.RetrieveExpenseGroupUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RetrieveExpenseGroupController.class)
@DisplayName("RetrieveExpenseGroupController | GET group by id")
class RetrieveExpenseGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrieveExpenseGroupUseCase retrieveExpenseGroupUseCase;

    @Test
    @DisplayName("should return 200 and group detail when group exists")
    void shouldReturn200AndGroupDetailWhenGroupExists() throws Exception {
        UUID groupId = UUID.randomUUID();
        ExpenseGroupDetailResponse response = new ExpenseGroupDetailResponse(
                groupId,
                "team-lunch",
                "alice@example.com",
                false,
                List.of(new ParticipantBalanceView(UUID.randomUUID(), "alice@example.com", BigDecimal.ZERO))
        );
        when(retrieveExpenseGroupUseCase.getExpenseGroup(groupId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/expense/groups/{id}", groupId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(groupId.toString()))
                .andExpect(jsonPath("$.name").value("team-lunch"))
                .andExpect(jsonPath("$.createdBy").value("alice@example.com"))
                .andExpect(jsonPath("$.activated").value(false))
                .andExpect(jsonPath("$.participants").isArray())
                .andExpect(jsonPath("$.participants.length()").value(1))
                .andExpect(jsonPath("$.participants[0].email").value("alice@example.com"))
                .andExpect(jsonPath("$.participants[0].balance").value(0));
    }

    @Test
    @DisplayName("should return 404 when group does not exist")
    void shouldReturn404WhenGroupNotFound() throws Exception {
        UUID groupId = UUID.randomUUID();
        when(retrieveExpenseGroupUseCase.getExpenseGroup(groupId))
                .thenThrow(new ExpenseGroupNotFoundException("Expense group not found: " + groupId));

        mockMvc.perform(get("/api/v1/expense/groups/{id}", groupId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").exists());
    }
}
