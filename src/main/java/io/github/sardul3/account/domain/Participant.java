package io.github.sardul3.account.domain;

import java.util.UUID;

public class Participant {
    private final UUID id;
    private final ParticipantEmail participantEmail;

    private Participant(UUID id, ParticipantEmail participantEmail) {
        this.id = id;
        this.participantEmail = participantEmail;
    }

    public static Participant withEmail(String email) {
        return new Participant(UUID.randomUUID(), ParticipantEmail.of(email));
    }

    public String getEmail() {
        return participantEmail.getEmail();
    }

    public UUID getId() {
        return id;
    }
}
