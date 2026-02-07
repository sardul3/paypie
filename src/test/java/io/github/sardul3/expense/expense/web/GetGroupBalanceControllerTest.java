package io.github.sardul3.expense.expense.web;

import io.github.sardul3.expense.adapter.in.web.controller.GetGroupBalanceController;
import io.github.sardul3.expense.application.dto.GroupBalanceResponse;
import io.github.sardul3.expense.application.dto.ParticipantBalanceView;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.in.GetGroupBalanceUseCase;
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

@WebMvcTest(GetGroupBalanceController.class)
@DisplayName("GetGroupBalanceController | GET group balance")
class GetGroupBalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetGroupBalanceUseCase getGroupBalanceUseCase;

    @Test
    @DisplayName("should return 200 and balance when group exists")
    void shouldReturn200AndBalanceWhenGroupExists() throws Exception {
        UUID groupId = UUID.randomUUID();
        GroupBalanceResponse response = new GroupBalanceResponse(
                groupId,
                List.of(
                        new ParticipantBalanceView(UUID.randomUUID(), "alice@example.com", BigDecimal.ZERO),
                        new ParticipantBalanceView(UUID.randomUUID(), "bob@example.com", new BigDecimal("-10.50"))
                )
        );
        when(getGroupBalanceUseCase.getBalance(groupId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/expense/groups/{id}/balance", groupId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(groupId.toString()))
                .andExpect(jsonPath("$.participants").isArray())
                .andExpect(jsonPath("$.participants.length()").value(2));
    }

    @Test
    @DisplayName("should return 404 when group does not exist")
    void shouldReturn404WhenGroupNotFound() throws Exception {
        UUID groupId = UUID.randomUUID();
        when(getGroupBalanceUseCase.getBalance(groupId))
                .thenThrow(new ExpenseGroupNotFoundException("Expense group not found: " + groupId));

        mockMvc.perform(get("/api/v1/expense/groups/{id}/balance", groupId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
