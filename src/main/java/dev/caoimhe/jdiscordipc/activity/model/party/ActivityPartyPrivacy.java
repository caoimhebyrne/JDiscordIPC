package dev.caoimhe.jdiscordipc.activity.model.party;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The privacy of an {@link ActivityParty}.
 */
public enum ActivityPartyPrivacy {
    /**
     * The party is private (or unknown), which means that the user is in a "party" but it is not joinable without
     * sending a request to join the party.
     * <p>
     * This is the default value. You will also receive this value when receiving other users' activities as the party
     * privacy for other users is not exposed.
     */
    PRIVATE,

    /**
     * The party is public, which means that the user is in a "party" which could be joinable by either friends or mutual
     * voice participants without sending a request to join the party.
     * <p>
     * This depends on a user's desired Discord account privacy settings.
     */
    PUBLIC;

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
