package io.github.sardul3.expense.adapter.in.web.controller;

import io.github.sardul3.expense.adapter.common.PrimaryAdapter;
import io.github.sardul3.expense.adapter.in.web.dto.SettleUpRequest;
import io.github.sardul3.expense.application.dto.SettleUpCommand;
import io.github.sardul3.expense.application.dto.SettleUpResponse;
import io.github.sardul3.expense.application.port.in.SettleUpUseCase;
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

import java.util.UUID;

@PrimaryAdapter
@RestController
@RequestMapping("/api/v1/expense")
public class SettleUpController {

    private static final Logger log = LoggerFactory.getLogger(SettleUpController.class);

    private final SettleUpUseCase settleUpUseCase;

    public SettleUpController(SettleUpUseCase settleUpUseCase) {
        this.settleUpUseCase = settleUpUseCase;
    }

    @PostMapping(value = "/groups/{id}/settle", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SettleUpResponse> settleUp(
            @PathVariable UUID id,
            @Valid @RequestBody SettleUpRequest request) {
        log.info("Settle up request: groupId={}, from={}, to={}, amount={}",
                id, request.fromParticipantId(), request.toParticipantId(), request.amount());
        SettleUpResponse response = settleUpUseCase.settleUp(id,
                new SettleUpCommand(request.fromParticipantId(), request.toParticipantId(), request.amount()));
        return ResponseEntity.ok(response);
    }
}
