package dev.caoimhe.jdiscordipc;

/**
 * The different states that a {@link JDiscordIPC} instance can be in.
 */
public enum JDiscordIPCState {
    /**
     * The user has not called {@link JDiscordIPC#connect()} yet. Or, the discord client has forcefully terminated the
     * connection.
     */
    DISCONNECTED,

    /**
     * A connection is currently being established with the Discord client.
     */
    CONNECTING,

    /**
     * The Discord client accepted the handshake and communication to/from the client can be performed.
     */
    READY;
}
