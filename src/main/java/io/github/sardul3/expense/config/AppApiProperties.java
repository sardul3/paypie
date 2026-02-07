package io.github.sardul3.expense.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Typed configuration for the API (base path, version). Keys under {@code app.api}.
 */
@ConfigurationProperties(prefix = "app.api")
@Validated
public record AppApiProperties(
        String basePath,
        String version
) {
    /**
     * Compact constructor; supplies defaults for null or blank values.
     */
    public AppApiProperties {
        if (basePath == null || basePath.isBlank()) {
            basePath = "/api/v1";
        }
        if (version == null || version.isBlank()) {
            version = "v1";
        }
    }
}
