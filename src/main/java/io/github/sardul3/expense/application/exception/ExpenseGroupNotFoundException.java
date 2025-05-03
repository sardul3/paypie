package io.github.sardul3.expense.application.exception;

public class ExpenseGroupNotFoundException extends RuntimeException {
    public ExpenseGroupNotFoundException(String message) {
        super(message);
    }
}