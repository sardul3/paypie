package io.github.sardul3.expense.adapter.in.web.controller;

import io.github.sardul3.expense.adapter.common.PrimaryAdapter;
import io.github.sardul3.expense.adapter.in.web.dto.AddParticipantRequest;
import io.github.sardul3.expense.application.dto.AddParticipantCommand;
import io.github.sardul3.expense.application.dto.AddParticipantResponse;
import io.github.sardul3.expense.application.port.in.AddParticipantUseCase;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@PrimaryAdapter
@RestController
@RequestMapping("/api/v1/expense")
public class AddParticipantController {

    private static final Logger log = LoggerFactory.getLogger(AddParticipantController.class);

    private final AddParticipantUseCase addParticipantUseCase;

    public AddParticipantController(AddParticipantUseCase addParticipantUseCase) {
        this.addParticipantUseCase = addParticipantUseCase;
    }

    @PostMapping(value = "/groups/{id}/participants", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddParticipantResponse> addParticipant(
            @PathVariable UUID id,
            @Valid @RequestBody AddParticipantRequest request) {
        log.info("Add participant request: groupId={}, email={}", id, request.email());
        AddParticipantResponse response = addParticipantUseCase.addParticipant(id, new AddParticipantCommand(request.email()));
        URI location = URI.create("/api/v1/expense/groups/" + id + "/participants/" + response.participantId());
        return ResponseEntity.created(location).body(response);
    }
}
