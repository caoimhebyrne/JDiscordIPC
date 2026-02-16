package dev.caoimhe.jdiscordipc;

import dev.caoimhe.jdiscordipc.activity.ActivityManager;
import dev.caoimhe.jdiscordipc.activity.model.Activity;
import dev.caoimhe.jdiscordipc.activity.model.ActivityBuilder;
import dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder;
import dev.caoimhe.jdiscordipc.event.DiscordEventListener;
import dev.caoimhe.jdiscordipc.event.model.Event;
import dev.caoimhe.jdiscordipc.event.model.ReadyEvent;
import dev.caoimhe.jdiscordipc.exception.JDiscordIPCException;
import dev.caoimhe.jdiscordipc.packet.Packet;
import dev.caoimhe.jdiscordipc.packet.PacketHandler;
import dev.caoimhe.jdiscordipc.packet.PacketManager;
import dev.caoimhe.jdiscordipc.packet.impl.HandshakePacket;
import dev.caoimhe.jdiscordipc.packet.impl.PingPacket;
import dev.caoimhe.jdiscordipc.packet.impl.PongPacket;
import dev.caoimhe.jdiscordipc.packet.impl.frame.incoming.DispatchEventPacket;
import dev.caoimhe.jdiscordipc.socket.SystemSocket;
import dev.caoimhe.jdiscordipc.util.CollectionsUtil;
import dev.caoimhe.jdiscordipc.util.SystemUtil;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * The main entrypoint for JDiscordIPC.
 * It's recommended to only have one instance of {@link JDiscordIPC} per-application. Having multiple concurrent
 * instances may lead to undefined behavior.
 *
 * @see dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder
 */
public class JDiscordIPC implements DiscordEventListener, PacketHandler {
    /**
     * A set of paths within the temporary directory that the Discord IPC socket may be located in. These cover common
     * packaging systems for Discord like Flatpak and Snap.
     */
    private static final Set<String> EXTRA_DISCORD_TEMP_PATHS = CollectionsUtil.setOf(
        // Flatpak
        "app/com.discordapp.Discord",
        "app/com.discordapp.DiscordCanary",

        // Snap
        "snap.discord",
        "snap.discord-canary",
        "snap.discord-ptb"
    );

    private JDiscordIPCState state;

    private final ActivityManager activityManager;
    private final long clientId;
    private final List<DiscordEventListener> eventListeners;
    private final PacketManager packetManager;
    private final ReconnectPolicy reconnectPolicy;
    private final SystemSocket systemSocket;

    /**
     * Initializes a new {@link JDiscordIPC} instance.
     *
     * @param clientId        The client ID to use when communicating with Discord.
     * @param reconnectPolicy How this instance should behave when the Discord client terminates the connection.
     * @param systemSocket    The system socket to read messages from and send messages to.
     * @see dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder
     */
    public JDiscordIPC(
        final long clientId,
        final ReconnectPolicy reconnectPolicy,
        final SystemSocket systemSocket
    ) {
        this.clientId = clientId;
        this.reconnectPolicy = reconnectPolicy;
        this.state = JDiscordIPCState.DISCONNECTED;
        this.systemSocket = systemSocket;

        // The most important thing is that JDiscordIPC is the first event listener. Its state is important to other
        // event listeners.
        this.eventListeners = new ArrayList<>();
        this.eventListeners.add(this);

        this.packetManager = new PacketManager(this, this.systemSocket);
        this.activityManager = new ActivityManager(this, this.packetManager);
    }

    /**
     * Connects to the running Discord application through the {@link SystemSocket} provided during initialization.
     * This method will block until the connection is initiated, but not until it is ready (see {@link ReadyEvent}).
     *
     * @throws JDiscordIPCException.DiscordClientUnavailableException When the connection could not be initiated.
     * @see JDiscordIPC#connect(ConnectOptions)
     */
    public void connect() throws JDiscordIPCException.DiscordClientUnavailableException {
        this.connect(ConnectOptions.builder().build());
    }

    /**
     * Connects to the running Discord application through the {@link SystemSocket} provided during initialization.
     * This method will block until the connection is initiated, but not until it is ready (see {@link ReadyEvent}).
     *
     * @throws JDiscordIPCException.DiscordClientUnavailableException When the connection could not be initiated.
     * @see ConnectOptions
     */
    public void connect(final ConnectOptions options) throws JDiscordIPCException.DiscordClientUnavailableException {
        try {
            this.state = JDiscordIPCState.CONNECTING;

            final Path discordIpcPath = this.getIpcFilePath(options);
            this.systemSocket.connect(discordIpcPath);

            // We can now tell the packet manager to start its packet reading thread.
            this.packetManager.startReadingIncomingPackets();

            // To mark the connection as ready, we must send a handshake to the client containing our client ID.
            this.packetManager.sendPacket(new HandshakePacket(this.clientId));
        } catch (final IOException e) {
            this.state = JDiscordIPCState.DISCONNECTED;
            throw new JDiscordIPCException.DiscordClientUnavailableException(e);
        }
    }

    /**
     * Queues an activity update for the current user.
     * <p>
     * If {@link #connect} has not been called, the {@link ActivityManager} will queue the {@link Activity} to be set
     * once the Discord client has connected (i.e. once {@link ReadyEvent} is dispatched).
     * <p>
     * If the Discord client reconnects after this method has been called, the user's activity will be reset to the
     * latest {@link Activity} value passed to this function.
     *
     * @param activity The activity to set on the user's profile. If null, the current activity belonging to this
     *                 application instance will be removed from the user's profile
     * @see ActivityManager#updateActivity(Activity)
     * @see Activity
     * @see ActivityBuilder
     */
    public void updateActivity(final @Nullable Activity activity) {
        this.activityManager.updateActivity(activity);
    }

    /**
     * Registers an event listener with this {@link JDiscordIPC} instance.
     * <p>
     * In order to ensure that your listener receives all events, this should be called before {@link #connect}.
     * Otherwise, events like {@link ReadyEvent} may be missed.
     *
     * @param listener The event listener instance to register.
     */
    public void registerEventListener(final DiscordEventListener listener) {
        this.eventListeners.add(listener);
    }

    /**
     * Returns the current state of this {@link JDiscordIPC} instance.
     */
    public JDiscordIPCState state() {
        return this.state;
    }

    @Override
    public void onReadyEvent(final ReadyEvent event) {
        // When the Discord client informs us that it is ready for communication, we can set the state to ready.
        this.state = JDiscordIPCState.READY;
    }

    /**
     * Returns a {@link JDiscordIPCBuilder} instance to construct a {@link JDiscordIPC} with.
     *
     * @param clientId The client ID to use when communicating with Discord.
     */
    public static JDiscordIPCBuilder builder(final long clientId) {
        return new JDiscordIPCBuilder(clientId);
    }

    @Override
    public void handlePacket(final Packet packet) {
        if (packet instanceof PingPacket) {
            final Map<String, Object> properties = ((PingPacket) packet).properties();
            this.packetManager.sendPacket(new PongPacket(properties));
        } else if (packet instanceof DispatchEventPacket) {
            final Event event = ((DispatchEventPacket) packet).data();

            try {
                this.eventListeners.forEach(it -> it.onEvent(event));
            } catch (final Exception e) {
                System.err.println("Failed to dispatch event to event listeners: " + e);
            }
        }
    }

    @Override
    public void handleEOF() {
        this.state = JDiscordIPCState.DISCONNECTED;

        if (this.reconnectPolicy == ReconnectPolicy.NEVER) {
            System.out.println("The Discord client terminated the connection. Not attempting to reconnect as the reconnect policy is set to never.");
            this.packetManager.stopReadingIncomingPackets();
        }
    }

    /**
     * Attempts to find a Unix domain socket file to connect to the Discord client in the system's temporary directory.
     * <p>
     * If {@link ConnectOptions#socketIndex()} is non-null, then that socket index will be used. Otherwise, socket
     * indices 0 through 9 will be checked.
     * <p>
     * If {@link ConnectOptions#temporaryDirectory()} is non-null, then it will be used instead of guessing the system's
     * temporary directory via {@link SystemUtil#getTemporaryDirectory()}.
     *
     * @throws JDiscordIPCException.DiscordClientUnavailableException If a Unix domain socket file could not be found.
     */
    private Path getIpcFilePath(final ConnectOptions options) throws JDiscordIPCException.DiscordClientUnavailableException {
        final Path temporaryDirectory;
        final Path temporaryDirectoryOverride = options.temporaryDirectory();

        if (temporaryDirectoryOverride != null) {
            temporaryDirectory = temporaryDirectoryOverride;
        } else if (SystemUtil.isWindows()) {
            temporaryDirectory = Paths.get("\\\\.\\pipe\\");
        } else {
            temporaryDirectory = SystemUtil.getTemporaryDirectory();
        }

        // The socket may be in the root of the temporary directory. Or, there are some subdirectories of the
        // temporary directory that it could be in (only on Unix).
        final Stream<Path> possibleParents;
        if (SystemUtil.isWindows()) {
            possibleParents = Stream.of(temporaryDirectory);
        } else {
            possibleParents = Stream.concat(
                Stream.of(temporaryDirectory),
                EXTRA_DISCORD_TEMP_PATHS.stream().map(temporaryDirectory::resolve)
            );
        }

        final Path ipcFilePath = possibleParents
            .map(it -> this.getIpcFilePath(options, it))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);

        if (ipcFilePath != null) {
            return ipcFilePath;
        }

        throw new JDiscordIPCException.DiscordClientUnavailableException(null);
    }

    /**
     * Attempts to find a Unix domain socket file to connect to the Discord client in the provided temporary
     * directory.
     * <p>
     * If {@link ConnectOptions#socketIndex()} is non-null, then that socket index will be used. Otherwise, socket
     * indices 0 through 9 will be checked.
     *
     * @param options            The {@link ConnectOptions} to consider.
     * @param temporaryDirectory The path to the temporary directory to check.
     * @return The path if it exists, otherwise null.
     */
    private @Nullable Path getIpcFilePath(final ConnectOptions options, final Path temporaryDirectory) {
        final Integer socketIndexOverride = options.socketIndex();
        if (socketIndexOverride != null) {
            final Path ipcFile = temporaryDirectory.resolve("discord-ipc-" + socketIndexOverride);
            if (Files.exists(ipcFile)) {
                return ipcFile;
            }

            return null;
        }

        for (int i = 0; i <= 9; i++) {
            final Path ipcFile = temporaryDirectory.resolve("discord-ipc-" + i);
            if (Files.exists(ipcFile)) {
                return ipcFile;
            }
        }

        return null;
    }
}
