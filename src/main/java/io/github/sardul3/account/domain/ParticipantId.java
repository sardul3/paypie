package io.github.sardul3.account.domain;

import java.util.UUID;

/**
 * Type-safe identifier for Participant entity.
 *
 */
public class ParticipantId extends BaseId<UUID>{
    private ParticipantId(UUID id) {
        super(id);
    }

    /**
     * Creates a new instance of ParticipantId from a UUID.
     *
     * @param id the UUID to wrap
     * @return a strongly typed ParticipantId
     */
    public static ParticipantId from(UUID id) {
        return new ParticipantId(id);
    }

    public static ParticipantId generate() {
        return new ParticipantId(UUID.randomUUID());
    }
}
