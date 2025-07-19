package dev.caoimhe.jdiscordipc.model.activity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Activity {
    private final String details;
    private final String state;

    @JsonCreator
    public Activity(
        final @JsonProperty("details") String details,
        final @JsonProperty("state") String state
    ) {
        this.details = details;
        this.state = state;
    }

    public String details() {
        return this.details;
    }

    public String state() {
        return this.state;
    }
}
