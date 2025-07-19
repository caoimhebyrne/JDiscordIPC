package dev.caoimhe.jdiscordipc.socket;

/**
 * An interface to be implemented by the platform which creates a new {@link SystemSocket} instance to be used for a
 * {@link dev.caoimhe.jdiscordipc.JDiscordIPC} instance.
 */
public interface SystemSocketFactory {
    /**
     * Creates a new {@link SystemSocket} instance.
     */
    SystemSocket createSystemSocket();
}
