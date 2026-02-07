package io.github.sardul3.expense.application.exception;

import org.springframework.http.HttpStatus;

/**
 * Base unchecked exception for application-layer errors. Carries a stable error code
 * and HTTP status for consistent API error mapping (e.g. to RFC 7807 ProblemDetail).
 */
public class BaseAppException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;
    private final String userMessage;

    public BaseAppException(ErrorCode errorCode, String message) {
        this(errorCode, errorCode.getDefaultStatus(), message, null);
    }

    public BaseAppException(ErrorCode errorCode, String message, Throwable cause) {
        this(errorCode, errorCode.getDefaultStatus(), message, cause);
    }

    public BaseAppException(ErrorCode errorCode, HttpStatus httpStatus, String userMessage) {
        this(errorCode, httpStatus, userMessage, null);
    }

    public BaseAppException(ErrorCode errorCode, HttpStatus httpStatus, String userMessage, Throwable cause) {
        super(userMessage, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.userMessage = userMessage != null ? userMessage : errorCode.name();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
