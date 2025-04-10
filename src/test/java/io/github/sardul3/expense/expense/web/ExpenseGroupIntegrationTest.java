package io.github.sardul3.expense.expense.web;

import io.github.sardul3.expense.adapter.in.web.dto.CreateExpenseGroupRequest;
import io.github.sardul3.expense.adapter.in.web.dto.ErrorResponse;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;
import io.github.sardul3.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
    void shouldThrowConflictErrorWithDuplicateName() {
        // First creation: should succeed
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("demo", "user@demo.com");
        ResponseEntity<CreateExpenseGroupResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, CreateExpenseGroupResponse.class
        );

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Second creation with same name: should fail with conflict
        ResponseEntity<ErrorResponse> conflictResponse = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, ErrorResponse.class
        );

        assertThat(conflictResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(conflictResponse.getBody()).isNotNull();
        assertThat(conflictResponse.getBody().status()).isEqualTo(409);
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

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isEqualTo("Validation Failed");
    }

    @Test
    void shouldReturn400BadRequestWithInvalidEmail() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("demo", "userdemo.com");

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isEqualTo("Validation Failed");
    }

    @Test
    void shouldRejectMalformedEmail() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("group2", "wrong-format");

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("Validation Failed");
    }

    @Test
    void shouldRejectTooLongGroupName() {
        String longName = "x".repeat(60);
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest(longName, "user@demo.com");

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("Illegal Argument");
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

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", invalidEntity, ErrorResponse.class);

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
}

