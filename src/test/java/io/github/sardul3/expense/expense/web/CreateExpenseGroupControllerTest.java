package io.github.sardul3.expense.expense.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sardul3.expense.adapter.in.web.controller.CreateExpenseGroupController;
import io.github.sardul3.expense.adapter.in.web.dto.CreateExpenseGroupRequest;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupAlreadyExistsException;
import io.github.sardul3.expense.application.port.in.CreateExpenseGroupUseCase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreateExpenseGroupController.class)
@DisplayName("ExpenseGroup | Web Controller Adapter Behavior")
class CreateExpenseGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateExpenseGroupUseCase createExpenseGroupUseCase;

    @Test
    @DisplayName("Expense Group | should succeed with no errors")
    void shouldReturn201CreatedHttpResponseEntityForHappyPath() throws Exception {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest(
                "demo", "user@demo.com"
        );

        when(createExpenseGroupUseCase.createExpenseGroup(any())).thenReturn(
                new CreateExpenseGroupResponse(UUID.randomUUID(), "demo")
        );

        mockMvc.perform(post("/api/v1/expense/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("demo"));
    }

    @Test
    @DisplayName("Expense Group | should return 400 Bad Request for invalid name")
    void shouldReturn400BadRequestForInvalidGroupName() throws Exception {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest(
                "", "user@demo.com"
        );

        mockMvc.perform(post("/api/v1/expense/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Expense Group | should return 400 Bad Request for invalid email")
    void shouldReturn400BadRequestForInvalidEmail() throws Exception {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest(
                "demo", "userdemo.com"
        );

        mockMvc.perform(post("/api/v1/expense/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Expense Group | should return 400 Bad Request for multiple invalid fields")
    void shouldReturn400BadRequestWithReadableMessageForUserForInvalidGroupName() throws Exception {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest(
                "", "user@demo.com"
        );

        mockMvc.perform(post("/api/v1/expense/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty())
                .andExpect(jsonPath("$.errors[0].message").value("cannot be empty"));

    }

    @Test
    @DisplayName("Expense Group | should return 400 Bad Request for multiple invalid fields")
    void shouldReturn400BadRequestWithReadableMessageForUserForInvalidEmail() throws Exception {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest(
                "demo", "userdemo.com"
        );

        mockMvc.perform(post("/api/v1/expense/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors[0].field").value("createdBy"))
                .andExpect(jsonPath("$.errors[0].message").isNotEmpty())
                .andExpect(jsonPath("$.errors[0].message").value("needs to be a valid email format"))
        ;
    }

    @Test
    @DisplayName("Expense Group | should return 400 Bad Request for multiple invalid fields")
    void shouldReturn400BadRequestWithReadableMessageForUserForInvalidPayload() throws Exception {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest(
                "", "userdemo.com"
        );

        mockMvc.perform(post("/api/v1/expense/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(2))
                .andExpect(jsonPath("$.errors[*].field",
                        containsInAnyOrder("createdBy", "name")))
                .andExpect(jsonPath("$.errors[*].message").isNotEmpty());
    }

    @Test
    @DisplayName("Expense Group | should return 409 for pre-existing expense group")
    void shouldReturn409ConflictWhenGroupAlreadyExists() throws Exception {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest(
                "demo", "user@demo.com"
        );

        when(createExpenseGroupUseCase.createExpenseGroup(any()))
                .thenThrow(new ExpenseGroupAlreadyExistsException("Group already exists"));

        mockMvc.perform(post("/api/v1/expense/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Group already exists"))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.title").value("CONFLICT"));
    }

    @Test
    @DisplayName("Expense Group | should return 400 for error bubbled up from domain layer [IAE]")
    void shouldReturn400BadRequestWhenIllegalArgumentIsThrown() throws Exception {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest(
                "demo", "user@demo.com"
        );

        when(createExpenseGroupUseCase.createExpenseGroup(any()))
                .thenThrow(new IllegalArgumentException("Invalid group parameters"));

        mockMvc.perform(post("/api/v1/expense/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid group parameters"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test
    @DisplayName("Expense Group | should return 400 for error bubbled up from domain layer [ISE]")
    void shouldReturn409ConflictWhenIllegalStateOccurs() throws Exception {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest(
                "demo", "user@demo.com"
        );

        when(createExpenseGroupUseCase.createExpenseGroup(any()))
                .thenThrow(new IllegalStateException("Group cannot be activated"));

        mockMvc.perform(post("/api/v1/expense/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Group cannot be activated"))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.title").value("Conflict"));
    }

    @Test
    @DisplayName("Expense Group | should return 400 for null fields")
    void shouldReturn400ForNullFields() throws Exception {
        String payload = """
        {
          "name": null,
          "createdBy": null
        }
        """;

        mockMvc.perform(post("/api/v1/expense/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("name", "createdBy")));
    }

    @Test
    @DisplayName("Expense Group | should return 400 for missing fields")
    void shouldReturn400ForMissingFields() throws Exception {
        String payload = """
        {
          "name": "demo"
        }
        """;

        mockMvc.perform(post("/api/v1/expense/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field").value("createdBy"));
    }
}
