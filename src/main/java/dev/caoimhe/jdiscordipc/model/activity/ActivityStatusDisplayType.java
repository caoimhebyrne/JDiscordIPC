package dev.caoimhe.jdiscordipc.model.activity;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Controls which field of the activity is displayed in the user's status text.
 */
public enum ActivityStatusDisplayType {
    /**
     * Shows the application's name in the user's status text.
     * <p>
     * Example: "Listening to Spotify"
     */
    APPLICATION_NAME,

    /**
     * Shows the activity's state field in the user's status text.
     * <p>
     * Example: "Listening to Rick Astley"
     */
    STATE,

    /**
     * Shows the activity's details field in the user's status text.
     * <p>
     * Example: "Listening to Never Gonna Give You Up"
     */
    DETAILS;

    @JsonValue
    public int getValue() {
        return this.ordinal();
    }
}
