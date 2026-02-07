package io.github.sardul3.expense.application.exception;

/**
 * Thrown when attempting to add a participant whose email is already in the expense group.
 */
public class ParticipantAlreadyInGroupException extends BaseAppException {
    public ParticipantAlreadyInGroupException(String message) {
        super(ErrorCode.CONFLICT, message);
    }
}
