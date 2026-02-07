package io.github.sardul3.expense.application.port.in;

import io.github.sardul3.expense.application.common.annotation.InputPort;
import io.github.sardul3.expense.application.dto.AddParticipantCommand;
import io.github.sardul3.expense.application.dto.AddParticipantResponse;

import java.util.UUID;

/**
 * Input port: adds a participant to an existing expense group.
 */
@InputPort(description = "Adds a new participant to an expense group by email")
public interface AddParticipantUseCase {

    /**
     * Adds a participant with the given email to the group.
     *
     * @param groupId the expense group id
     * @param command contains participant email
     * @return response with participant id and email
     * @throws io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException if group not found
     * @throws io.github.sardul3.expense.application.exception.ParticipantAlreadyInGroupException if email already in group
     */
    AddParticipantResponse addParticipant(UUID groupId, AddParticipantCommand command);
}
