package dev.caoimhe.jdiscordipc;

/**
 * Determines how the {@link JDiscordIPC} should re-connect to the Discord client after its connection is closed.
 *
 * @see dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder#reconnectPolicy(ReconnectPolicy)
 */
public enum ReconnectPolicy {
    /**
     * Don't reconnect under any circumstances.
     */
    NEVER;
}
