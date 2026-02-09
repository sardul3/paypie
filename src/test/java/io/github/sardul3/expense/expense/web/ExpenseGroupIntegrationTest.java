package io.github.sardul3.expense.expense.web;

import io.github.sardul3.expense.adapter.in.web.dto.AddParticipantRequest;
import io.github.sardul3.expense.adapter.in.web.dto.CreateExpenseGroupRequest;
import io.github.sardul3.expense.adapter.in.web.dto.SettleUpRequest;
import io.github.sardul3.expense.adapter.in.web.dto.ValidationErrorResponse;
import io.github.sardul3.expense.application.dto.AddParticipantResponse;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;
import io.github.sardul3.expense.domain.valueobject.GroupName;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;
import io.github.sardul3.expense.application.dto.ExpenseGroupDetailResponse;
import io.github.sardul3.expense.application.dto.ExpenseHistoryPageResponse;
import io.github.sardul3.expense.application.dto.GroupBalanceResponse;
import io.github.sardul3.expense.application.dto.SettleUpResponse;
import org.springframework.http.ProblemDetail;
import io.github.sardul3.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Tag("integration")
@TestPropertySource(properties = "integration.context=web")
class ExpenseGroupIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ExpenseGroupRepository repository;

    @Test
    void shouldCreateExpenseGroupSuccessfully() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("success-demo", "user@demo.com");

        ResponseEntity<CreateExpenseGroupResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, CreateExpenseGroupResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("success-demo");
    }

    @Test
    void shouldGetExpenseGroupByIdAfterCreate() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("get-by-id-group", "creator@example.com");
        ResponseEntity<CreateExpenseGroupResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, CreateExpenseGroupResponse.class
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        var groupId = createResponse.getBody().id();

        ResponseEntity<ExpenseGroupDetailResponse> getResponse = restTemplate.getForEntity(
                "/api/v1/expense/groups/" + groupId, ExpenseGroupDetailResponse.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().id()).isEqualTo(groupId);
        assertThat(getResponse.getBody().name()).isEqualTo("get-by-id-group");
        assertThat(getResponse.getBody().createdBy()).isEqualTo("creator@example.com");
        assertThat(getResponse.getBody().participants()).hasSize(1);
        assertThat(getResponse.getBody().participants().get(0).email()).isEqualTo("creator@example.com");
    }

    @Test
    void shouldReturn404WhenGettingNonExistentGroup() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON, MediaType.parseMediaType("application/problem+json")));
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(
                "/api/v1/expense/groups/00000000-0000-0000-0000-000000000000",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                ProblemDetail.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    void shouldThrowConflictErrorWithDuplicateName() {
        // First creation: should succeed
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("demo", "user@demo.com");
        ResponseEntity<CreateExpenseGroupResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, CreateExpenseGroupResponse.class
        );

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Second creation with same name: should fail with conflict
        HttpHeaders conflictHeaders = new HttpHeaders();
        conflictHeaders.setContentType(MediaType.APPLICATION_JSON);
        conflictHeaders.setAccept(java.util.List.of(MediaType.APPLICATION_JSON, MediaType.parseMediaType("application/problem+json")));
        ResponseEntity<ProblemDetail> conflictResponse = restTemplate.exchange(
                "/api/v1/expense/groups",
                HttpMethod.POST,
                new HttpEntity<>(request, conflictHeaders),
                ProblemDetail.class
        );

        assertThat(conflictResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(conflictResponse.getBody()).isNotNull();
        assertThat(conflictResponse.getBody().getStatus()).isEqualTo(409);
    }

    @Test
    void shouldCreateExpenseGroupSuccessfullyAndReturnLocationHeader() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("demo-header", "user@demo.com");

        ResponseEntity<CreateExpenseGroupResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, CreateExpenseGroupResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("demo-header");
        assertThat(response.getHeaders().getLocation().toString()).startsWith("/api/v1/expense/groups");
    }

    @Test
    void shouldReturn400BadRequestWithInvalidNameMessage() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("", "user@demo.com");

        ResponseEntity<ValidationErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, ValidationErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isEqualTo("Validation Failed");
    }

    @Test
    void shouldReturn400BadRequestWithInvalidEmail() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("demo", "userdemo.com");

        ResponseEntity<ValidationErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, ValidationErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isEqualTo("Validation Failed");
    }

    @Test
    void shouldRejectMalformedEmail() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("group2", "wrong-format");

        ResponseEntity<ValidationErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, ValidationErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("Validation Failed");
    }

    @Test
    void shouldRejectTooLongGroupName() {
        String longName = "x".repeat(60);
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest(longName, "user@demo.com");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON, MediaType.parseMediaType("application/problem+json")));
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(
                "/api/v1/expense/groups", HttpMethod.POST, new HttpEntity<>(request, headers), ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getDetail()).contains("longer");
    }

    @Test
    void shouldNormalizeAndAcceptEmailWithUpperCase() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("case-test", "UsEr@Demo.Com");

        ResponseEntity<CreateExpenseGroupResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, CreateExpenseGroupResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("case-test");
    }

    @Test
    void shouldRejectUnsupportedMediaType() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> invalidEntity = new HttpEntity<>("invalid body", headers);

        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", invalidEntity, ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Test
    void shouldTrimWhitespaceInGroupName() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("   neat-group  ", "user@demo.com");

        ResponseEntity<CreateExpenseGroupResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, CreateExpenseGroupResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("neat-group");
    }

    @Test
    void shouldAddParticipantAndPersistThenReturnInGetGroup() {
        CreateExpenseGroupRequest createRequest = new CreateExpenseGroupRequest("add-participant-group", "owner@example.com");
        ResponseEntity<CreateExpenseGroupResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups", createRequest, CreateExpenseGroupResponse.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        var groupId = createResponse.getBody().id();

        AddParticipantRequest addRequest = new AddParticipantRequest("member@example.com");
        ResponseEntity<AddParticipantResponse> addResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups/" + groupId + "/participants", addRequest, AddParticipantResponse.class);
        assertThat(addResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(addResponse.getBody()).isNotNull();
        assertThat(addResponse.getBody().email()).isEqualTo("member@example.com");
        assertThat(addResponse.getBody().participantId()).isNotNull();

        ResponseEntity<ExpenseGroupDetailResponse> getResponse = restTemplate.getForEntity(
                "/api/v1/expense/groups/" + groupId, ExpenseGroupDetailResponse.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().participants()).hasSize(2);
        assertThat(getResponse.getBody().participants().stream().map(p -> p.email()).toList())
                .containsExactlyInAnyOrder("owner@example.com", "member@example.com");
    }

    @Test
    void shouldReturn404WhenAddingParticipantToNonExistentGroup() {
        var groupId = java.util.UUID.randomUUID();
        AddParticipantRequest addRequest = new AddParticipantRequest("someone@example.com");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON, MediaType.parseMediaType("application/problem+json")));
        ResponseEntity<ProblemDetail> response = restTemplate.exchange(
                "/api/v1/expense/groups/" + groupId + "/participants",
                HttpMethod.POST,
                new HttpEntity<>(addRequest, headers),
                ProblemDetail.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    void shouldReturn409WhenAddingDuplicateParticipant() {
        CreateExpenseGroupRequest createRequest = new CreateExpenseGroupRequest("dup-participant-group", "dup@example.com");
        ResponseEntity<CreateExpenseGroupResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups", createRequest, CreateExpenseGroupResponse.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        var groupId = createResponse.getBody().id();

        AddParticipantRequest addRequest = new AddParticipantRequest("dup@example.com");
        HttpHeaders conflictHeaders = new HttpHeaders();
        conflictHeaders.setContentType(MediaType.APPLICATION_JSON);
        conflictHeaders.setAccept(java.util.List.of(MediaType.APPLICATION_JSON, MediaType.parseMediaType("application/problem+json")));
        ResponseEntity<ProblemDetail> conflictResponse = restTemplate.exchange(
                "/api/v1/expense/groups/" + groupId + "/participants",
                HttpMethod.POST,
                new HttpEntity<>(addRequest, conflictHeaders),
                ProblemDetail.class);
        assertThat(conflictResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(conflictResponse.getBody()).isNotNull();
        assertThat(conflictResponse.getBody().getStatus()).isEqualTo(409);
    }

    @Test
    void shouldGetGroupBalanceAfterCreateAndAddParticipant() {
        CreateExpenseGroupRequest createRequest = new CreateExpenseGroupRequest("balance-group", "payer@example.com");
        ResponseEntity<CreateExpenseGroupResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups", createRequest, CreateExpenseGroupResponse.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        var groupId = createResponse.getBody().id();

        restTemplate.postForEntity(
                "/api/v1/expense/groups/" + groupId + "/participants",
                new AddParticipantRequest("member@example.com"),
                AddParticipantResponse.class);

        ResponseEntity<GroupBalanceResponse> balanceResponse = restTemplate.getForEntity(
                "/api/v1/expense/groups/" + groupId + "/balance", GroupBalanceResponse.class);
        assertThat(balanceResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(balanceResponse.getBody()).isNotNull();
        assertThat(balanceResponse.getBody().groupId()).isEqualTo(groupId);
        assertThat(balanceResponse.getBody().participants()).hasSize(2);
    }

    @Test
    void shouldSettleUpAndPersistBalances() {
        CreateExpenseGroupRequest createRequest = new CreateExpenseGroupRequest("settle-group", "alice@example.com");
        ResponseEntity<CreateExpenseGroupResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups", createRequest, CreateExpenseGroupResponse.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        var groupId = createResponse.getBody().id();

        restTemplate.postForEntity(
                "/api/v1/expense/groups/" + groupId + "/participants",
                new AddParticipantRequest("bob@example.com"),
                AddParticipantResponse.class);
        var groupDetail = restTemplate.getForEntity("/api/v1/expense/groups/" + groupId, ExpenseGroupDetailResponse.class).getBody();
        assertThat(groupDetail).isNotNull();
        var aliceId = groupDetail.participants().stream().filter(p -> "alice@example.com".equals(p.email())).findFirst().get().participantId();
        var bobId = groupDetail.participants().stream().filter(p -> "bob@example.com".equals(p.email())).findFirst().get().participantId();

        SettleUpRequest settleRequest = new SettleUpRequest(bobId, aliceId, java.math.BigDecimal.TEN);
        ResponseEntity<SettleUpResponse> settleResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups/" + groupId + "/settle", settleRequest, SettleUpResponse.class);
        assertThat(settleResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(settleResponse.getBody()).isNotNull();
        assertThat(settleResponse.getBody().groupId()).isEqualTo(groupId);

        ResponseEntity<GroupBalanceResponse> balanceResponse = restTemplate.getForEntity(
                "/api/v1/expense/groups/" + groupId + "/balance", GroupBalanceResponse.class);
        assertThat(balanceResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(balanceResponse.getBody()).isNotNull();
        var bobBalance = balanceResponse.getBody().participants().stream().filter(p -> p.participantId().equals(bobId)).findFirst().get().balance();
        var aliceBalance = balanceResponse.getBody().participants().stream().filter(p -> p.participantId().equals(aliceId)).findFirst().get().balance();
        assertThat(bobBalance).isEqualByComparingTo(java.math.BigDecimal.TEN);
        assertThat(aliceBalance).isEqualByComparingTo(java.math.BigDecimal.valueOf(-10));
    }

    @Test
    void shouldGetExpenseHistoryPaginated() {
        CreateExpenseGroupRequest createRequest = new CreateExpenseGroupRequest("history-group", "alice@example.com");
        ResponseEntity<CreateExpenseGroupResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups", createRequest, CreateExpenseGroupResponse.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        var groupId = createResponse.getBody().id();

        ResponseEntity<ExpenseHistoryPageResponse> historyResponse = restTemplate.getForEntity(
                "/api/v1/expense/groups/" + groupId + "/activities?page=0&size=20", ExpenseHistoryPageResponse.class);
        assertThat(historyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(historyResponse.getBody()).isNotNull();
        assertThat(historyResponse.getBody().content()).isEmpty();
        assertThat(historyResponse.getBody().totalElements()).isZero();
        assertThat(historyResponse.getBody().size()).isEqualTo(20);
    }

    @Test
    void shouldSaveAndRetrieveAnExpenseGroup() {
        ExpenseGroup group = ExpenseGroup.from(GroupName.withName("demo"), Participant.withEmail("a@b.com"));
        ExpenseGroup saved = repository.save(group);
        assertThat(saved).isNotNull();
        assertThat(repository.existsByName(GroupName.withName("demo"))).isTrue();
    }

    @Test
    void shouldReturnFalseWhenGroupNameDoesNotExist() {
        boolean exists = repository.existsByName(GroupName.withName("non-existent"));
        assertThat(exists).isFalse();
    }

    @Test
    void dbContainerStarted() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldCreateExpenseGroupWithValidJsonInput() throws Exception {
        CreateExpenseGroupRequest request = loadTestDataFromJson(
                "test-data/create-expense-group.json",
                CreateExpenseGroupRequest.class);

        ResponseEntity<CreateExpenseGroupResponse> response = performPost(
                "/api/v1/expense/groups",
                request,
                CreateExpenseGroupResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo(request.name());
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION)).isNotNull();
        assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION)).contains("/api/v1/expense/groups/");
    }

    @Test
    void shouldThrowExceptionWithInvalidNameInput() throws Exception {
        CreateExpenseGroupRequest request = loadTestDataFromJson(
                "test-data/create-expense-group-invalid-name.json",
                CreateExpenseGroupRequest.class);

        ResponseEntity<ValidationErrorResponse> response = performPost(
                "/api/v1/expense/groups",
                request,
                ValidationErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldThrowExceptionWithCorrectMessageForMissingField() throws Exception {
        CreateExpenseGroupRequest request = loadTestDataFromJson(
                "test-data/create-expense-group-missing-field-name.json",
                CreateExpenseGroupRequest.class);

        ResponseEntity<ValidationErrorResponse> response = performPost(
                "/api/v1/expense/groups",
                request,
                ValidationErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getBody().errors().get(0).field()).isEqualTo("name");
        assertThat(response.getBody().errors().get(0).message()).isEqualTo("cannot be empty");
    }
}

