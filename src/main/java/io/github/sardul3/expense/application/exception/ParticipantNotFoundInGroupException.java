package io.github.sardul3.expense.application.exception;

/**
 * Thrown when a participant referenced in an operation is not a member of the expense group.
 */
public class ParticipantNotFoundInGroupException extends BaseAppException {
    public ParticipantNotFoundInGroupException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}
