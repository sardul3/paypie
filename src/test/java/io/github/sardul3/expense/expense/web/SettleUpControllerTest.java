package io.github.sardul3.expense.expense.web;

import io.github.sardul3.expense.adapter.in.web.controller.SettleUpController;
import io.github.sardul3.expense.application.dto.SettleUpResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.in.SettleUpUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SettleUpController.class)
@DisplayName("SettleUpController | POST settle up")
class SettleUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SettleUpUseCase settleUpUseCase;

    @Test
    @DisplayName("should return 200 and group id when settlement recorded")
    void shouldReturn200WhenSettlementRecorded() throws Exception {
        UUID groupId = UUID.randomUUID();
        UUID fromId = UUID.randomUUID();
        UUID toId = UUID.randomUUID();
        when(settleUpUseCase.settleUp(eq(groupId), any())).thenReturn(new SettleUpResponse(groupId));

        mockMvc.perform(post("/api/v1/expense/groups/{id}/settle", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fromParticipantId":"%s","toParticipantId":"%s","amount":25.50}
                                """.formatted(fromId, toId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(groupId.toString()));
    }

    @Test
    @DisplayName("should return 404 when group not found")
    void shouldReturn404WhenGroupNotFound() throws Exception {
        UUID groupId = UUID.randomUUID();
        when(settleUpUseCase.settleUp(eq(groupId), any()))
                .thenThrow(new ExpenseGroupNotFoundException("Expense group not found: " + groupId));

        mockMvc.perform(post("/api/v1/expense/groups/{id}/settle", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fromParticipantId":"%s","toParticipantId":"%s","amount":10}
                                """.formatted(UUID.randomUUID(), UUID.randomUUID())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 when amount is zero")
    void shouldReturn400WhenAmountZero() throws Exception {
        UUID groupId = UUID.randomUUID();
        mockMvc.perform(post("/api/v1/expense/groups/{id}/settle", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fromParticipantId":"%s","toParticipantId":"%s","amount":0}
                                """.formatted(UUID.randomUUID(), UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }
}
