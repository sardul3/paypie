package io.github.sardul3.account.domain;

import java.util.UUID;

/**
 * Type-safe identifier for Participant entity.
 */
public class ParticipantId extends BaseId<UUID>{
    private ParticipantId(UUID id) {
        super(id);
    }

    public static ParticipantId from(UUID id) {
        return new ParticipantId(id);
    }

    public static ParticipantId of(UUID id) {
        return new ParticipantId(id);
    }

    public static ParticipantId generate() {
        return new ParticipantId(UUID.randomUUID());
    }
}
