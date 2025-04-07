package io.github.sardul3.expense.application.exception;

public class ExpenseGroupAlreadyExistsException extends RuntimeException {
    public ExpenseGroupAlreadyExistsException(String message) {
        super(message);
    }
}
