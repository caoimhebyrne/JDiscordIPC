package dev.caoimhe.jdiscordipc.exception;

import org.jspecify.annotations.Nullable;

/**
 * Thrown by {@link dev.caoimhe.jdiscordipc.JDiscordIPC} when it is unable to find a running Discord client to connect to.
 */
public class DiscordClientUnavailableException extends Exception {
    public DiscordClientUnavailableException(final @Nullable Exception cause) {
        super("Failed to find a running Discord client. Is discord open?", cause);
    }
}
