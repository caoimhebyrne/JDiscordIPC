package dev.caoimhe.jdiscordipc.activity.model;

import org.jspecify.annotations.Nullable;

import java.time.Instant;

/**
 * A pair of timestamps indicating the start and/or end time of an {@link Activity}.
 * <p>
 * For {@link ActivityType#LISTENING} and {@link ActivityType#WATCHING} activities, you can include both timestamps
 * to display a time progress bar on the user's profile.
 */
public class ActivityTimestamps {
    private final Long start;
    private final @Nullable Long end;

    /**
     * Initializes a new {@link ActivityTimestamps} instance from a starting now.
     */
    public static ActivityTimestamps indefiniteFromNow() {
        return ActivityTimestamps.indefiniteFrom(Instant.now());
    }

    /**
     * Initializes a new {@link ActivityTimestamps} instance from a starting {@link Instant}.
     *
     * @param start The {@link Instant} that the activity started at.
     */
    public static ActivityTimestamps indefiniteFrom(final Instant start) {
        return ActivityTimestamps.from(start, null);
    }

    /**
     * Initializes a new {@link ActivityTimestamps} instance using {@link Instant}s.
     *
     * @param start The {@link Instant} that the activity started at.
     * @param end   The (optional) {@link Instant} that the activity should end at.
     */
    public static ActivityTimestamps from(final Instant start, final @Nullable Instant end) {
        return new ActivityTimestamps(start.toEpochMilli(), end != null ? end.toEpochMilli() : null);
    }

    /**
     * Initializes a new {@link ActivityTimestamps} instance.
     *
     * @param start The Unix timestamp (in milliseconds) that the activity started at.
     * @param end   The (optional) Unix timestamp (in milliseconds) that the activity should end at.
     */
    public ActivityTimestamps(final long start, final @Nullable Long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * The Unix timestamp that this {@link Activity} started at.
     */
    public Long start() {
        return this.start;
    }

    /**
     * The Unix timestamp that this {@link Activity} ended at.
     */
    public @Nullable Long end() {
        return this.end;
    }
}
