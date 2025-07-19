package dev.caoimhe.jdiscordipc.activity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.caoimhe.jdiscordipc.activity.model.assets.ActivityAssets;
import dev.caoimhe.jdiscordipc.activity.model.party.ActivityParty;
import org.jspecify.annotations.Nullable;

/**
 * An activity being sent to the Discord client to be displayed on the user's profile.
 *
 * @see ActivityBuilder
 * @see Activity#Activity(ActivityType, String, String, String, String, ActivityStatusDisplayType, ActivityParty, ActivityTimestamps, ActivityAssets)
 */
public class Activity {
    @JsonProperty("type")
    private final ActivityType type;

    @JsonProperty("details")
    private final @Nullable String details;

    @JsonProperty("details_url")
    private final @Nullable String detailsUrl;

    @JsonProperty("state")
    private final @Nullable String state;

    @JsonProperty("state_url")
    private final @Nullable String stateUrl;

    @JsonProperty("status_display_type")
    private final @Nullable ActivityStatusDisplayType statusDisplayType;

    @JsonProperty("party")
    private final @Nullable ActivityParty party;

    @JsonProperty("timestamps")
    private final @Nullable ActivityTimestamps timestamps;

    @JsonProperty("assets")
    private final @Nullable ActivityAssets assets;

    /**
     * Initializes a new {@link Activity} instance.
     *
     * @param type              The type of activity that this is, see {@link ActivityType}.
     * @param details           The state of what the user is doing for this activity.
     * @param detailsUrl        The URL that is linked when clicking on the details text.
     * @param state             The state of the party for this activity. Must be between 2 and 128 characters.
     * @param stateUrl          The URL that is linked when clicking on the state text.
     * @param statusDisplayType Controls which field should be shown in the user's status text, see {@link ActivityStatusDisplayType}.
     * @param party             The party attached to this activity.
     * @param timestamps        A pair of timestamps indicating when the activity starts and ends.
     * @param assets            The assets to display to users in the activity card.
     */
    public Activity(
        final ActivityType type,
        final @Nullable String details,
        final @Nullable String detailsUrl,
        final @Nullable String state,
        final @Nullable String stateUrl,
        final @Nullable ActivityStatusDisplayType statusDisplayType,
        final @Nullable ActivityParty party,
        final @Nullable ActivityTimestamps timestamps,
        final @Nullable ActivityAssets assets
    ) {
        this.type = type;
        this.details = details;
        this.detailsUrl = detailsUrl;
        this.state = state;
        this.stateUrl = stateUrl;
        this.statusDisplayType = statusDisplayType;
        this.party = party;
        this.timestamps = timestamps;
        this.assets = assets;
    }

    @JsonCreator
    protected Activity() {
        this(ActivityType.PLAYING, null, null, null, null, null, null, null, null);
    }

    /**
     * Returns a {@link ActivityBuilder} instance to construct an {@link Activity} with.
     */
    public static ActivityBuilder builder() {
        return new ActivityBuilder();
    }
}
