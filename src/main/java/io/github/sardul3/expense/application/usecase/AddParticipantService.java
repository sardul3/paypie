package io.github.sardul3.expense.application.usecase;

import io.github.sardul3.expense.application.common.annotation.UseCase;
import io.github.sardul3.expense.application.dto.AddParticipantCommand;
import io.github.sardul3.expense.application.dto.AddParticipantResponse;
import io.github.sardul3.expense.application.exception.ExpenseGroupNotFoundException;
import io.github.sardul3.expense.application.exception.ParticipantAlreadyInGroupException;
import io.github.sardul3.expense.application.port.in.AddParticipantUseCase;
import io.github.sardul3.expense.application.port.out.ExpenseGroupRepository;
import io.github.sardul3.expense.domain.model.ExpenseGroup;
import io.github.sardul3.expense.domain.model.Participant;

import java.util.Objects;
import java.util.UUID;

/**
 * Use case: adds a participant to an existing expense group. Persists the updated group.
 */
@UseCase(description = "Adds a participant to an expense group by email", inputPort = AddParticipantUseCase.class)
public class AddParticipantService implements AddParticipantUseCase {

    private final ExpenseGroupRepository expenseGroupRepository;

    public AddParticipantService(ExpenseGroupRepository expenseGroupRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
    }

    @Override
    public AddParticipantResponse addParticipant(UUID groupId, AddParticipantCommand command) {
        if (groupId == null) {
            throw new IllegalArgumentException("groupId cannot be null");
        }
        Objects.requireNonNull(command, "AddParticipantCommand cannot be null");

        ExpenseGroup group = expenseGroupRepository.findById(groupId)
                .orElseThrow(() -> new ExpenseGroupNotFoundException("Expense group not found: " + groupId));

        boolean alreadyMember = group.getParticipants().stream()
                .anyMatch(p -> p.getEmail().equalsIgnoreCase(command.email()));
        if (alreadyMember) {
            throw new ParticipantAlreadyInGroupException("Participant with email " + command.email() + " is already in the group");
        }

        Participant newParticipant = Participant.withEmail(command.email());
        group.addParticipant(newParticipant);
        expenseGroupRepository.save(group);

        return new AddParticipantResponse(newParticipant.getParticipantId().getId(), newParticipant.getEmail());
    }
}
