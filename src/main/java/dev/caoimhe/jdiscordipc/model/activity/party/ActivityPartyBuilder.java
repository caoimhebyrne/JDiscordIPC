package dev.caoimhe.jdiscordipc.model.activity.party;

import org.jspecify.annotations.Nullable;

/**
 * Provides a builder for {@link ActivityParty} instances.
 */
public class ActivityPartyBuilder {
    /**
     * The unique ID of this party.
     */
    private final String id;

    /**
     * The number of people currently in this party, must be at least 1.
     */
    private final int currentSize;

    /**
     * The maximum number of people that can be in this party, must be at least 0. When 0, the UI will not display a maximum.
     * <p>
     * Defaults to 0.
     */
    private int maximumSize;

    /**
     * The privacy of this party, defaults to {@link ActivityPartyPrivacy#PRIVATE}.
     */
    private ActivityPartyPrivacy privacy;

    /**
     * Initializes a new {@link ActivityPartyBuilder}.
     *
     * @param id          The unique ID of this party.
     * @param currentSize The number of people currently in this party, must be at least 1
     */
    public ActivityPartyBuilder(final String id, final int currentSize) {
        this.id = id;
        this.currentSize = currentSize;
        this.maximumSize = 0;
        this.privacy = ActivityPartyPrivacy.PRIVATE;
    }

    /**
     * Sets the maximum number of people that can be in this party. Must be at least 0.
     * <p>
     * When set to 0, the UI will not display a maximum party size.
     */
    public ActivityPartyBuilder maximumSize(final int maximumSize) {
        this.maximumSize = maximumSize;
        return this;
    }

    /**
     * Sets the privacy of this party.
     *
     * @see ActivityPartyPrivacy
     */
    public ActivityPartyBuilder privacy(ActivityPartyPrivacy privacy) {
        this.privacy = privacy;
        return this;
    }

    /**
     * Builds an {@link ActivityParty} instance from this builder.
     */
    public ActivityParty build() {
        return new ActivityParty(this.id, this.currentSize, this.maximumSize, this.privacy);
    }
}
