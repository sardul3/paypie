package io.github.sardul3.account.domain;

public class Participant {
    private final ParticipantEmail participantEmail;

    private Participant(ParticipantEmail participantEmail) {
        this.participantEmail = participantEmail;
    }

    public static Participant withEmail(String email) {
        return new Participant(ParticipantEmail.of(email));
    }

    public String getEmail() {
        return participantEmail.getEmail();
    }
}
