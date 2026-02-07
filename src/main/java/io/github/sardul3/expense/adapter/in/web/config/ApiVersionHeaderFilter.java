package io.github.sardul3.expense.adapter.in.web.config;

import io.github.sardul3.expense.config.AppApiProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Adds X-API-Version response header from app.api.version configuration.
 */
@Component
public class ApiVersionHeaderFilter extends OncePerRequestFilter {

    private static final String X_API_VERSION = "X-API-Version";

    private final AppApiProperties appApiProperties;

    public ApiVersionHeaderFilter(AppApiProperties appApiProperties) {
        this.appApiProperties = appApiProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        response.setHeader(X_API_VERSION, appApiProperties.version());
        filterChain.doFilter(request, response);
    }
}
