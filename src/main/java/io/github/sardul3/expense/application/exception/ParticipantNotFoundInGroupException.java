package io.github.sardul3.expense.application.exception;

public class ParticipantNotFoundInGroupException extends RuntimeException {
    public ParticipantNotFoundInGroupException(String message) {
        super(message);
    }
}
