package io.github.sardul3.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sardul3.expense.PayPieApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.InputStream;

@Testcontainers
@SpringBootTest(
        classes = PayPieApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractIntegrationTest {

    @Container
    protected static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void registerDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    protected <T> T loadTestDataFromJson(String resourcePath, Class<T> valueType) throws IOException {
        try (InputStream is = new ClassPathResource(resourcePath).getInputStream()) {
            return objectMapper.readValue(is, valueType);
        }
    }

    protected <T, R> ResponseEntity<R> performPost(String path, T requestBody, Class<R> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForEntity(getBaseUrl() + path, requestEntity, responseType);
    }

    protected <T, R> ResponseEntity<R> performPostWithJsonData(String path, String jsonDataPath,
                                                               Class<T> requestType, Class<R> responseType)
            throws IOException {
        T requestBody = loadTestDataFromJson(jsonDataPath, requestType);
        return performPost(path, requestBody, responseType);
    }
}
