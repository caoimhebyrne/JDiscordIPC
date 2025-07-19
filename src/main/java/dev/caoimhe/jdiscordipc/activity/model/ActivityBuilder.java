package dev.caoimhe.jdiscordipc.activity.model;

import dev.caoimhe.jdiscordipc.activity.model.party.ActivityParty;
import dev.caoimhe.jdiscordipc.activity.model.party.ActivityPartyBuilder;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A builder for a {@link Activity} instance.
 * <p>
 * The only requirement for using this builder is that you either set {@link #details(String)} or {@link #state(String)},
 * otherwise, Discord will reject the activity.
 */
public class ActivityBuilder {
    /**
     * The type of {@link Activity} being built.
     * <p>
     * Defaults to {@link ActivityType#PLAYING}.
     */
    private ActivityType type;

    /**
     * The state of <i>what the user is doing</i> for this activity, also known as the "activity details".
     */
    private @Nullable String details;

    /**
     * The URL that is linked when clicking on the details text.
     */
    private @Nullable String detailsUrl;

    /**
     * The state of <i>the party/i> for this activity.
     */
    private @Nullable String state;

    /**
     * The URL that is linked when clicking on the state text.
     */
    private @Nullable String stateUrl;

    /**
     * Controls which field of the activity is displayed in the user's status text.
     */
    private @Nullable ActivityStatusDisplayType statusDisplayType;

    /**
     * The party attached to this activity.
     */
    private @Nullable ActivityParty party;

    /**
     * A pair of timestamps indicating when the {@link Activity} starts and ends.
     */
    private @Nullable ActivityTimestamps timestamps;

    /**
     * Initializes a new {@link ActivityBuilder} instance with the following defaults:
     * <ul>
     *     <li>{@link #type}: {@link ActivityType#PLAYING}.</li>
     * </ul>
     */
    public ActivityBuilder() {
        this.type = ActivityType.PLAYING;
    }

    /**
     * Sets the {@link ActivityType} for the built activity instance.
     */
    public ActivityBuilder type(final ActivityType type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the state of <i>what the user is doing</i> for this activity, also known as the "activity details".
     */
    public ActivityBuilder details(final @Nullable String details) {
        this.details = details;
        return this;
    }

    /**
     * Sets the state of <i>what the user is doing</i> for this activity, also known as the "activity details".
     *
     * @param url The URL that is linked when clicking on the details text.
     */
    public ActivityBuilder details(final @Nullable String details, final @Nullable String url) {
        this.details = details;
        this.detailsUrl = url;
        return this;
    }

    /**
     * Sets the state of <i>the party</i> for this activity.
     */
    public ActivityBuilder state(final @Nullable String state) {
        this.state = state;
        return this;
    }

    /**
     * Sets the state of <i>the party</i> for this activity.
     *
     * @param url The URL that is linked when clicking on the state text.
     */
    public ActivityBuilder state(final @Nullable String state, final @Nullable String url) {
        this.state = state;
        this.stateUrl = url;
        return this;
    }

    /**
     * Sets which field of the activity is displayed in the user's status text.
     * <p>
     * The default behavior is {@link ActivityStatusDisplayType#APPLICATION_NAME}.
     *
     * @see ActivityStatusDisplayType
     */
    public ActivityBuilder statusDisplayType(final @Nullable ActivityStatusDisplayType statusDisplayType) {
        this.statusDisplayType = statusDisplayType;
        return this;
    }

    /**
     * Sets the {@link ActivityParty} for the built activity.
     *
     * @see ActivityParty
     */
    public ActivityBuilder party(final @Nullable ActivityParty party) {
        this.party = party;
        return this;
    }

    /**
     * Sets the {@link ActivityParty} for the built activity using a {@link ActivityPartyBuilder}.
     *
     * @param id              The unique ID of this party.
     * @param currentSize     The number of people currently in this party, must be at least 1
     * @param builderConsumer The {@link Consumer} to call to customize the {@link ActivityPartyBuilder}.
     */
    public ActivityBuilder party(
        final String id,
        final int currentSize,
        final Consumer<ActivityPartyBuilder> builderConsumer
    ) {
        final ActivityPartyBuilder builder = ActivityParty.builder(id, currentSize);
        builderConsumer.accept(builder);

        this.party = builder.build();
        return this;
    }

    /**
     * Sets the {@link ActivityTimestamps} for the built activity.
     */
    public ActivityBuilder timestamps(final @Nullable ActivityTimestamps timestamps) {
        this.timestamps = timestamps;
        return this;
    }

    /**
     * Constructs an {@link Activity} instance with the information contained within this builder.
     */
    public Activity build() {
        // TODO: Verify that state or details was set.
        return new Activity(
            this.type,
            this.details,
            this.detailsUrl,
            this.state,
            this.stateUrl,
            this.statusDisplayType,
            this.party,
            this.timestamps
        );
    }
}
