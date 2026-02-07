package io.github.sardul3.expense.expense.web;

import io.github.sardul3.expense.adapter.in.web.dto.AddParticipantRequest;
import io.github.sardul3.expense.adapter.in.web.dto.CreateExpenseGroupRequest;
import io.github.sardul3.expense.adapter.in.web.dto.ValidationErrorResponse;
import io.github.sardul3.expense.application.dto.AddParticipantResponse;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;
import io.github.sardul3.expense.application.dto.ExpenseGroupDetailResponse;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Tag("integration")
class ExpenseGroupIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
        ResponseEntity<ProblemDetail> response = restTemplate.getForEntity(
                "/api/v1/expense/groups/00000000-0000-0000-0000-000000000000", ProblemDetail.class
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
        ResponseEntity<ProblemDetail> conflictResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, ProblemDetail.class
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

        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, ProblemDetail.class);

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
        ResponseEntity<ProblemDetail> response = restTemplate.postForEntity(
                "/api/v1/expense/groups/" + groupId + "/participants", addRequest, ProblemDetail.class);
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
        ResponseEntity<ProblemDetail> conflictResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups/" + groupId + "/participants", addRequest, ProblemDetail.class);
        assertThat(conflictResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(conflictResponse.getBody()).isNotNull();
        assertThat(conflictResponse.getBody().getStatus()).isEqualTo(409);
    }
}

