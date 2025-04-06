package io.github.sardul3.account.domain;

public class ParticipantEmail {
    private final String email;

    private ParticipantEmail(String email) {
        this.email = email;
    }

    public static ParticipantEmail of(String email) {
        return new ParticipantEmail(email);
    }

    public String getEmail() {
        return email;
    }
}
