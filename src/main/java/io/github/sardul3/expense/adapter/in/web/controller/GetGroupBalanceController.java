package io.github.sardul3.expense.adapter.in.web.controller;

import io.github.sardul3.expense.adapter.common.PrimaryAdapter;
import io.github.sardul3.expense.application.dto.GroupBalanceResponse;
import io.github.sardul3.expense.application.port.in.GetGroupBalanceUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@PrimaryAdapter
@RestController
@RequestMapping("/api/v1/expense")
public class GetGroupBalanceController {

    private static final Logger log = LoggerFactory.getLogger(GetGroupBalanceController.class);

    private final GetGroupBalanceUseCase getGroupBalanceUseCase;

    public GetGroupBalanceController(GetGroupBalanceUseCase getGroupBalanceUseCase) {
        this.getGroupBalanceUseCase = getGroupBalanceUseCase;
    }

    @GetMapping(value = "/groups/{id}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupBalanceResponse> getGroupBalance(@PathVariable UUID id) {
        log.info("Get group balance request: groupId={}", id);
        GroupBalanceResponse response = getGroupBalanceUseCase.getBalance(id);
        return ResponseEntity.ok(response);
    }
}
