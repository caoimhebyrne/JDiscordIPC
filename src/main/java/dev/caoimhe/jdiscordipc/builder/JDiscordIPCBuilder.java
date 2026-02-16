package dev.caoimhe.jdiscordipc.builder;

import dev.caoimhe.jdiscordipc.JDiscordIPC;
import dev.caoimhe.jdiscordipc.ReconnectPolicy;
import dev.caoimhe.jdiscordipc.exception.JDiscordIPCBuilderException;
import dev.caoimhe.jdiscordipc.socket.SystemSocketFactory;
import dev.caoimhe.jdiscordipc.socket.impl.WindowsSystemSocketFactory;
import dev.caoimhe.jdiscordipc.internal.util.SystemUtil;
import org.jspecify.annotations.Nullable;

/**
 * A builder for {@link JDiscordIPC} instances.
 *
 * @see JDiscordIPC#builder(long)
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
     * <p>
     * The provided {@link SystemSocketFactory} instance is only used on Unix operating systems. On Windows,
     * {@link WindowsSystemSocketFactory} will always be used regardless of whether this method is called.
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
        final SystemSocketFactory systemSocketFactory;

        // On Windows, we always need to use the Windows system socket factory, this is not customizable.
        if (SystemUtil.isWindows()) {
            systemSocketFactory = new WindowsSystemSocketFactory();
        } else {
            systemSocketFactory = this.systemSocketFactory;
        }

        // If a system socket factory could not be created, we need to throw an exception.
        if (systemSocketFactory == null) {
            throw new JDiscordIPCBuilderException.MissingSystemSocketFactoryException();
        }

        return new JDiscordIPC(this.clientId, this.reconnectPolicy, systemSocketFactory.createSystemSocket());
    }
}
