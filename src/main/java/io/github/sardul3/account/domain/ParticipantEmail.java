package io.github.sardul3.account.domain;

public class ParticipantEmail {
    private final String email;

    private ParticipantEmail(String email) {
        if(email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if(!isValid(email)) {
            throw new IllegalArgumentException("Email is not valid - Invalid email format");
        }
        final String normalizedEmail = email.toLowerCase();
        this.email = normalizedEmail;
    }

    public static ParticipantEmail of(String email) {
        return new ParticipantEmail(email);
    }

    public String getEmail() {
        return email;
    }

    private static boolean isValid(String email) {
        if (email.isEmpty()) return false;

        // Simple regex to validate basic email format:
        // - Starts with alphanumeric characters (including + _ . -)
        // - Followed by @
        // - Followed by domain (letters, digits, dots and hyphens)
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
