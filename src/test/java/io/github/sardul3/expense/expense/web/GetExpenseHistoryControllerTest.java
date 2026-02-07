package io.github.sardul3.expense.expense.web;

import io.github.sardul3.expense.adapter.in.web.controller.GetExpenseHistoryController;
import io.github.sardul3.expense.application.dto.ActivityView;
import io.github.sardul3.expense.application.dto.ExpenseHistoryPageResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.port.in.GetExpenseHistoryUseCase;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetExpenseHistoryController.class)
@DisplayName("GetExpenseHistoryController | GET expense history")
class GetExpenseHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetExpenseHistoryUseCase getExpenseHistoryUseCase;

    @Test
    @DisplayName("should return 200 and paginated content when group exists")
    void shouldReturn200AndPaginatedContent() throws Exception {
        UUID groupId = UUID.randomUUID();
        ExpenseHistoryPageResponse response = new ExpenseHistoryPageResponse(
                List.of(new ActivityView(UUID.randomUUID(), "Lunch", BigDecimal.valueOf(30), UUID.randomUUID(), true)),
                1L, 1, 0, 20
        );
        when(getExpenseHistoryUseCase.getExpenseHistory(eq(groupId), eq(0), eq(20))).thenReturn(response);

        mockMvc.perform(get("/api/v1/expense/groups/{id}/activities", groupId)
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].description").value("Lunch"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(20));
    }

    @Test
    @DisplayName("should return 404 when group not found")
    void shouldReturn404WhenGroupNotFound() throws Exception {
        UUID groupId = UUID.randomUUID();
        when(getExpenseHistoryUseCase.getExpenseHistory(eq(groupId), anyInt(), anyInt()))
                .thenThrow(new ExpenseGroupNotFoundException("Expense group not found: " + groupId));

        mockMvc.perform(get("/api/v1/expense/groups/{id}/activities", groupId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
