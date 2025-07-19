package dev.caoimhe.jdiscordipc.model.activity;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The different types of {@link Activity} that are supported.
 *
 * @see <a href="https://discord.com/developers/docs/social-sdk/namespacediscordpp.html#a6c76a8cbbc9270f025fd6854d5558660">Discord Documentation</a>.
 */
public enum ActivityType {
    /**
     * Indicates that the user is playing a game. Example: "Playing (application name)".
     */
    PLAYING(0),

    /**
     * Indicates that the user is listening to something. Example: "Listening to (application name)".
     */
    LISTENING(2),

    /**
     * Indicates that the user is watching something. Example: "Watching (application name)".
     */
    WATCHING(3),

    /**
     * Indicates that the user is competing in something. Example: "Competing in (application name)".
     */
    COMPETING(5);

    private final int value;

    ActivityType(final int value) {
        this.value = value;
    }

    @JsonValue
    public int toValue() {
        return this.value;
    }
}
