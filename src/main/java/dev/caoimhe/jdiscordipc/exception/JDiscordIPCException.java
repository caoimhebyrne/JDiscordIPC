package dev.caoimhe.jdiscordipc.exception;

import org.jspecify.annotations.Nullable;

/**
 * A base class for all JDiscordIPC exceptions to implement.
 */
public class JDiscordIPCException extends Exception {
    private JDiscordIPCException(final String message, final @Nullable Throwable cause) {
        super(message, cause);
    }

    /**
     * Thrown when a connection could not be established with the Discord client, typically due to it being closed or a
     * lack of permissions.
     */
    public static class DiscordClientUnavailableException extends JDiscordIPCException {
        public DiscordClientUnavailableException(final @Nullable Throwable cause) {
            super("Unable to establish a connection to the Discord client", cause);
        }
    }
}
