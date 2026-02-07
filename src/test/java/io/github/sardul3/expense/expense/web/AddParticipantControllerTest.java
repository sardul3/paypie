package io.github.sardul3.expense.expense.web;

import io.github.sardul3.expense.adapter.in.web.controller.AddParticipantController;
import io.github.sardul3.expense.application.dto.AddParticipantResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.exception.ParticipantAlreadyInGroupException;
import io.github.sardul3.expense.application.port.in.AddParticipantUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddParticipantController.class)
@DisplayName("AddParticipantController | POST add participant")
class AddParticipantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddParticipantUseCase addParticipantUseCase;

    @Test
    @DisplayName("should return 201 and Location when participant added")
    void shouldReturn201AndLocationWhenParticipantAdded() throws Exception {
        UUID groupId = UUID.randomUUID();
        UUID participantId = UUID.randomUUID();
        AddParticipantResponse response = new AddParticipantResponse(participantId, "bob@example.com");

        when(addParticipantUseCase.addParticipant(eq(groupId), any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/expense/groups/{id}/participants", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"bob@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.participantId").value(participantId.toString()))
                .andExpect(jsonPath("$.email").value("bob@example.com"));
    }

    @Test
    @DisplayName("should return 404 when group does not exist")
    void shouldReturn404WhenGroupNotFound() throws Exception {
        UUID groupId = UUID.randomUUID();
        when(addParticipantUseCase.addParticipant(eq(groupId), any()))
                .thenThrow(new ExpenseGroupNotFoundException("Expense group not found: " + groupId));

        mockMvc.perform(post("/api/v1/expense/groups/{id}/participants", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"bob@example.com\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("should return 409 when participant already in group")
    void shouldReturn409WhenParticipantAlreadyInGroup() throws Exception {
        UUID groupId = UUID.randomUUID();
        when(addParticipantUseCase.addParticipant(eq(groupId), any()))
                .thenThrow(new ParticipantAlreadyInGroupException(
                        "Participant with email bob@example.com is already in the group"));

        mockMvc.perform(post("/api/v1/expense/groups/{id}/participants", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"bob@example.com\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("should return 400 when email is invalid")
    void shouldReturn400WhenEmailInvalid() throws Exception {
        UUID groupId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/expense/groups/{id}/participants", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when email is blank")
    void shouldReturn400WhenEmailBlank() throws Exception {
        UUID groupId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/expense/groups/{id}/participants", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
