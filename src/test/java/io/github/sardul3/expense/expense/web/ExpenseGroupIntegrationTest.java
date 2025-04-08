package io.github.sardul3.expense.expense.web;

import io.github.sardul3.expense.adapter.in.web.dto.CreateExpenseGroupRequest;
import io.github.sardul3.expense.adapter.in.web.dto.ErrorResponse;
import io.github.sardul3.expense.adapter.out.persistence.InMemoryExpenseGroupRepository;
import io.github.sardul3.expense.application.dto.CreateExpenseGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebTestClient
class ExpenseGroupIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateExpenseGroupSuccessfully() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("demo", "user@demo.com");

        ResponseEntity<CreateExpenseGroupResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, CreateExpenseGroupResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("demo");
    }

    @Test
    void shouldCreateExpenseGroupSuccessfullyAndReturnLocationHeader() {
        CreateExpenseGroupRequest request = new CreateExpenseGroupRequest("demo", "user@demo.com");

        ResponseEntity<CreateExpenseGroupResponse> response = restTemplate.postForEntity(
                "/api/v1/expense/groups", request, CreateExpenseGroupResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("demo");
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
}

