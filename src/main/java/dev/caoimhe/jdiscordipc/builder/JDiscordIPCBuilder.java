package dev.caoimhe.jdiscordipc.builder;

import dev.caoimhe.jdiscordipc.JDiscordIPC;
import dev.caoimhe.jdiscordipc.ReconnectPolicy;
import dev.caoimhe.jdiscordipc.core.SystemSocketFactory;
import dev.caoimhe.jdiscordipc.exception.JDiscordIPCBuilderException;
import org.jspecify.annotations.Nullable;

/**
 * A builder for {@link JDiscordIPC} instances.
 *
 * @see #of
 */
public class JDiscordIPCBuilder {
    /**
     * The client ID to use when communicating with Discord.
     */
    private final long clientId;

    /**
     * The {@link ReconnectPolicy} that should be used when the Discord client terminates the connection.
     */
    private ReconnectPolicy reconnectPolicy;

    /**
     * The {@link SystemSocketFactory} implementation to use when building the {@link JDiscordIPC} instance.
     *
     * @see #systemSocketFactory(SystemSocketFactory)
     */
    private @Nullable SystemSocketFactory systemSocketFactory;

    /**
     * Initializes a new {@link JDiscordIPCBuilder}.
     *
     * @param clientId The client ID to use when communicating with Discord.
     */
    public JDiscordIPCBuilder(final long clientId) {
        this.clientId = clientId;
        this.reconnectPolicy = ReconnectPolicy.NEVER;
        this.systemSocketFactory = null;
    }

    /**
     * Sets the {@link ReconnectPolicy} that should be used when the Discord client terminates the connection.
     */
    public JDiscordIPCBuilder reconnectPolicy(final ReconnectPolicy reconnectPolicy) {
        this.reconnectPolicy = reconnectPolicy;
        return this;
    }

    /**
     * Sets the {@link SystemSocketFactory} implementation to use when building the {@link JDiscordIPC} instance.
     *
     * @see SystemSocketFactory
     */
    public JDiscordIPCBuilder systemSocketFactory(final SystemSocketFactory systemSocketFactory) {
        this.systemSocketFactory = systemSocketFactory;
        return this;
    }

    /**
     * Constructs a {@link JDiscordIPC} instance from this builder.
     * This does not connect to the Discord socket yet, call {@link JDiscordIPC#connect} to initialize the connection.
     *
     * @throws JDiscordIPCBuilderException.MissingSystemSocketFactoryException If {@link #systemSocketFactory(SystemSocketFactory)}
     *                                                                         is not called.
     */
    public JDiscordIPC build() {
        if (this.systemSocketFactory == null) {
            throw new JDiscordIPCBuilderException.MissingSystemSocketFactoryException();
        }

        return new JDiscordIPC(this.clientId, this.reconnectPolicy, this.systemSocketFactory.createSystemSocket());
    }
}
