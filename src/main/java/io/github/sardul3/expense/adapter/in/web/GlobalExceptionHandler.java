package io.github.sardul3.expense.adapter.in.web;

import io.github.sardul3.expense.adapter.in.web.dto.FieldErrorResponse;
import io.github.sardul3.expense.adapter.in.web.dto.ValidationErrorResponse;
import io.github.sardul3.expense.application.exception.BaseAppException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.List;

/**
 * Global exception handler that maps exceptions to RFC 7807 ProblemDetail for API errors
 * and structured validation responses for constraint violations.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String PROBLEM_TYPE_URI_PREFIX = "https://errors.paypie.io/";

    @ExceptionHandler(BaseAppException.class)
    public ResponseEntity<org.springframework.http.ProblemDetail> handleBaseAppException(
            BaseAppException ex, HttpServletRequest request) {
        org.springframework.http.ProblemDetail problem = org.springframework.http.ProblemDetail.forStatusAndDetail(
                ex.getHttpStatus(), ex.getUserMessage());
        problem.setTitle(ex.getErrorCode().name());
        problem.setType(URI.create(PROBLEM_TYPE_URI_PREFIX + ex.getErrorCode().name()));
        problem.setProperty("errorCode", ex.getErrorCode().name());
        problem.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(ex.getHttpStatus())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new FieldErrorResponse(err.getField(), err.getDefaultMessage()))
                .toList();
        ValidationErrorResponse body = ValidationErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                request.getRequestURI(),
                errors
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ValidationErrorResponse> handleBindException(
            BindException ex, HttpServletRequest request) {
        List<FieldErrorResponse> errors = ex.getFieldErrors().stream()
                .map(err -> new FieldErrorResponse(err.getField(), err.getDefaultMessage()))
                .toList();
        ValidationErrorResponse body = ValidationErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Binding Error",
                request.getRequestURI(),
                errors
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<org.springframework.http.ProblemDetail> handleUnsupportedMediaType(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        org.springframework.http.ProblemDetail problem = org.springframework.http.ProblemDetail
                .forStatusAndDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage());
        problem.setTitle("Unsupported Media Type");
        problem.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<org.springframework.http.ProblemDetail> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        org.springframework.http.ProblemDetail problem = org.springframework.http.ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Bad Request");
        problem.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<org.springframework.http.ProblemDetail> handleIllegalState(
            IllegalStateException ex, HttpServletRequest request) {
        org.springframework.http.ProblemDetail problem = org.springframework.http.ProblemDetail
                .forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Conflict");
        problem.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<org.springframework.http.ProblemDetail> handleException(
            Exception ex, HttpServletRequest request) {
        org.springframework.http.ProblemDetail problem = org.springframework.http.ProblemDetail
                .forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problem.setTitle("Internal Server Error");
        problem.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
