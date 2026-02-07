package io.github.sardul3.expense.application.exception;

/**
 * Thrown when an expense group cannot be created because a group with the same name already exists.
 */
public class ExpenseGroupAlreadyExistsException extends BaseAppException {
    public ExpenseGroupAlreadyExistsException(String message) {
        super(ErrorCode.CONFLICT, message);
    }
}
