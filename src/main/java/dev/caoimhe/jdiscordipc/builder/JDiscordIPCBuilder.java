package dev.caoimhe.jdiscordipc.builder;

import dev.caoimhe.jdiscordipc.JDiscordIPC;
import dev.caoimhe.jdiscordipc.core.SystemSocket;
import dev.caoimhe.jdiscordipc.core.SystemSocketFactory;
import dev.caoimhe.jdiscordipc.exception.MissingSystemSocketFactoryException;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A builder for {@link JDiscordIPC} instances.
 *
 * @see JDiscordIPCBuilder#of
 */
public class JDiscordIPCBuilder {
    /**
     * The client ID to use when communicating with Discord.
     */
    private final long clientId;

    /**
     * The {@link SystemSocketFactory} implementation to use when building the {@link JDiscordIPC} instance.
     *
     * @see JDiscordIPCBuilder#systemSocketFactory(SystemSocketFactory)
     */
    private @Nullable SystemSocketFactory systemSocketFactory;

    /**
     * The {@link ExecutorService} to use for starting background tasks, e.g. reading from the {@link SystemSocket}.
     */
    private ExecutorService executorService;

    private JDiscordIPCBuilder(final long clientId) {
        this.clientId = clientId;
        this.executorService = Executors.newCachedThreadPool();
        this.systemSocketFactory = null;
    }

    /**
     * Initializes a new {@link JDiscordIPCBuilder}.
     *
     * @param clientId The client ID to use when communicating with Discord.
     */
    public static JDiscordIPCBuilder of(final long clientId) {
        return new JDiscordIPCBuilder(clientId);
    }

    /**
     * Sets the {@link Executor} implementation to use when starting background tasks.
     */
    public JDiscordIPCBuilder executorService(final ExecutorService executorService) {
        this.executorService = executorService;
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
     * @throws MissingSystemSocketFactoryException If {@link JDiscordIPCBuilder#systemSocketFactory(SystemSocketFactory)}
     *                                             is not called.
     */
    public JDiscordIPC build() {
        if (this.systemSocketFactory == null) {
            throw new MissingSystemSocketFactoryException();
        }

        return new JDiscordIPC(this.clientId, this.executorService, this.systemSocketFactory.createSystemSocket());
    }
}
