package dev.caoimhe.jdiscordipc.model.activity.party;

import dev.caoimhe.jdiscordipc.model.activity.Activity;

/**
 * A party within a {@link Activity}.
 * <p>
 * "Party" is used colloquially to refer to a group of players in a shared context. This could be a lobby ID, team ID,
 * server ID, etc.
 */
public class ActivityParty {
    /**
     * The unique ID of this party.
     */
    private final String id;

    /**
     * An array containing the current and maximum size of this party.
     */
    private final int[] size;

    /**
     * The privacy of this activity party.
     */
    private final ActivityPartyPrivacy privacy;

    /**
     * Initializes a new {@link ActivityParty} instance.
     *
     * @param id          The unique ID of this party.
     * @param currentSize The number of people currently in this party, must be at least 1.
     * @param maxSize     The maximum number of people that can be in this party, must be at least 0. If set to 0, the UI
     *                    will not display a maximum size.
     * @param privacy     The privacy of this party, see {@link ActivityPartyPrivacy}.
     */
    public ActivityParty(final String id, final int currentSize, final int maxSize, final ActivityPartyPrivacy privacy) {
        this.id = id;
        this.size = new int[]{currentSize, maxSize};
        this.privacy = privacy;
    }

    /**
     * Returns an {@link ActivityPartyBuilder} to construct an {@link ActivityParty} with.
     *
     * @param id          The unique ID of this party.
     * @param currentSize The number of people currently in this party, must be at least 1
     */
    public static ActivityPartyBuilder builder(final String id, final int currentSize) {
        return new ActivityPartyBuilder(id, currentSize);
    }
}
