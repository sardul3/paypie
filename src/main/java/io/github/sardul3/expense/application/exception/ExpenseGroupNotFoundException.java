package io.github.sardul3.expense.application.exception;

/**
 * Thrown when a requested expense group does not exist.
 */
public class ExpenseGroupNotFoundException extends BaseAppException {
    public ExpenseGroupNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}