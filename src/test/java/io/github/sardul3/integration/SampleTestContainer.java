package io.github.sardul3.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sardul3.expense.PayPieApplication;
import io.github.sardul3.expense.adapter.in.web.dto.CreateExpenseGroupRequest;
import io.github.sardul3.expense.adapter.in.web.dto.ErrorResponse;
import io.github.sardul3.expense.adapter.in.web.dto.ValidationErrorResponse;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(
        classes = PayPieApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SampleTestContainer extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer("postgres:15-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void dbContainerStarted() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldThrowExceptionWithInvalidNameInput() throws IOException {
        CreateExpenseGroupRequest request = loadTestDataFromJson(
                "test-data/create-expense-group-invalid-name.json",
                CreateExpenseGroupRequest.class
        );

        ResponseEntity<ErrorResponse> response = performPost(
                "/api/v1/expense/groups",
                request,
                ErrorResponse.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldThrowExceptionWithCorrectMessageForMissingField() throws IOException {
        CreateExpenseGroupRequest request = loadTestDataFromJson(
                "test-data/create-expense-group-missing-field-name.json",
                CreateExpenseGroupRequest.class
        );

        ResponseEntity<ValidationErrorResponse> response = performPost(
                "/api/v1/expense/groups",
                request,
                ValidationErrorResponse.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getBody().errors().get(0).field()).isEqualTo("name");
        assertThat(response.getBody().errors().get(0).message()).isEqualTo("cannot be empty");
    }

    @Test
    void shouldCreateExpenseGroupWithValidJsonInput() throws Exception {
        // Given a request loaded from JSON
        CreateExpenseGroupRequest request = loadTestDataFromJson(
                "/test-data/create-expense-group.json",
                CreateExpenseGroupRequest.class
        );

        // When making a POST request
        ResponseEntity<CreateExpenseGroupResponse> response = performPost(
                "/api/v1/expense/groups",
                request,
                CreateExpenseGroupResponse.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo(request.name());
        assertThat(response.getBody().id()).isNotNull();
        // Verify Location header
        String locationHeader = response.getHeaders().getFirst("Location");
        assertThat(locationHeader).isNotNull();
        assertThat(locationHeader).contains("/api/v1/expense/groups/");
    }
}
