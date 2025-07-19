package dev.caoimhe.jdiscordipc;

import dev.caoimhe.jdiscordipc.activity.ActivityManager;
import dev.caoimhe.jdiscordipc.activity.model.Activity;
import dev.caoimhe.jdiscordipc.activity.model.ActivityBuilder;
import dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder;
import dev.caoimhe.jdiscordipc.core.SystemSocket;
import dev.caoimhe.jdiscordipc.core.codec.PacketCodec;
import dev.caoimhe.jdiscordipc.core.packet.Packet;
import dev.caoimhe.jdiscordipc.core.packet.impl.HandshakePacket;
import dev.caoimhe.jdiscordipc.core.packet.impl.PingPacket;
import dev.caoimhe.jdiscordipc.core.packet.impl.PongPacket;
import dev.caoimhe.jdiscordipc.core.packet.impl.frame.OutgoingFramePacket;
import dev.caoimhe.jdiscordipc.core.packet.impl.frame.incoming.DispatchEventPacket;
import dev.caoimhe.jdiscordipc.event.DiscordEventListener;
import dev.caoimhe.jdiscordipc.event.model.Event;
import dev.caoimhe.jdiscordipc.event.model.ReadyEvent;
import dev.caoimhe.jdiscordipc.exception.JDiscordIPCException;
import dev.caoimhe.jdiscordipc.util.SystemUtil;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The main entrypoint for JDiscordIPC.
 * It's recommended to only have one instance of {@link JDiscordIPC} per-application. Having multiple concurrent
 * instances may lead to undefined behavior.
 *
 * @see dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder
 */
public class JDiscordIPC implements DiscordEventListener {
    private @Nullable Thread backgroundThread;
    private JDiscordIPCState state;

    private final ActivityManager activityManager;
    private final long clientId;
    private final List<DiscordEventListener> eventListeners;
    private final PacketCodec packetCodec;
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
        this.packetCodec = PacketCodec.from(systemSocket);
        this.reconnectPolicy = reconnectPolicy;
        this.state = JDiscordIPCState.DISCONNECTED;
        this.systemSocket = systemSocket;

        // The most important thing is that JDiscordIPC is the first event listener. Its state is important to other
        // event listeners.
        this.eventListeners = new ArrayList<>();
        this.registerEventListener(this);

        this.activityManager = new ActivityManager(this);
    }

    /**
     * Connects to the running Discord application through the {@link SystemSocket} provided during initialization.
     * This method will block until the connection is initiated, but not until it is ready (see {@link ReadyEvent}).
     *
     * @throws JDiscordIPCException.DiscordClientUnavailableException When the connection could not be initiated.
     */
    public void connect() throws JDiscordIPCException.DiscordClientUnavailableException {
        try {
            this.state = JDiscordIPCState.CONNECTING;

            final Path discordIpcPath = this.getIpcFilePath();
            this.systemSocket.connect(discordIpcPath);

            // If an existing thread is already running, let's get rid of it and start a new one.
            if (this.backgroundThread != null) {
                this.backgroundThread.interrupt();
            }

            // Now that we've connected, we can start a background task to start consuming messages from Discord.
            this.backgroundThread = new Thread(this::readPackets, "JDiscordIPC-Packet-Reading");
            this.backgroundThread.setDaemon(false);
            this.backgroundThread.start();

            // To mark the connection as ready, we must send a handshake to the client containing our client ID.
            this.packetCodec.write(new HandshakePacket(this.clientId));
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
     * Sends a {@link OutgoingFramePacket} to the Discord client if it is in the ready state.
     * <p>
     * If {@link #connect} has not been called, or, the Discord client has terminated the connection, this will not
     * send anything.
     */
    public void sendPacket(final OutgoingFramePacket<?> packet) {
        if (this.state != JDiscordIPCState.READY) {
            return;
        }

        try {
            this.packetCodec.write(packet);
        } catch (IOException e) {
            // TODO
        }
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

    /**
     * Responsible for reading packets from the Discord client and ensuring they get handled.
     */
    private void readPackets() {
        while (this.systemSocket.isConnected()) {
            // If the current thread has been interrupted, we can just return immediately.
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            final Packet packet;
            try {
                packet = this.packetCodec.read();
            } catch (final Exception e) {
                System.err.println("Failed to read packet: " + e);
                continue;
            }

            // If a null value is returned, the socket was closed by the other side, we should handle this gracefully.
            if (packet == null) {
                break;
            }

            try {
                this.handlePacket(packet);
            } catch (final Exception e) {
                System.err.println("Failed to handle packet: " + e);
            }
        }

        // If the while loop ends, we have been disconnected from the client.
        this.handleDisconnect();
    }

    /**
     * Called when the Discord client terminates the connection (either gracefully or forcefully).
     */
    private void handleDisconnect() {
        this.state = JDiscordIPCState.DISCONNECTED;

        if (this.reconnectPolicy == ReconnectPolicy.NEVER) {
            System.out.println("The Discord client terminated the connection. Not attempting to reconnect as the reconnect policy is set to never.");

            if (this.backgroundThread != null) {
                this.backgroundThread.interrupt();
            }
        }
    }

    /**
     * Handles an incoming packet from the Discord client.
     *
     * @param packet The packet to handle.
     * @see #readPackets
     */
    private void handlePacket(final Packet packet) {
        if (packet instanceof PingPacket) {
            try {
                final Map<String, Object> properties = ((PingPacket) packet).properties();
                this.packetCodec.write(new PongPacket(properties));
            } catch (final IOException e) {
                System.err.println("Failed to write pong packet: " + e);
            }
        } else if (packet instanceof DispatchEventPacket) {
            final Event event = ((DispatchEventPacket) packet).data();

            try {
                this.eventListeners.forEach(it -> it.onEvent(event));
            } catch (final Exception e) {
                System.err.println("Failed to dispatch event to event listeners: " + e);
            }
        }
    }

    /**
     * Attempts to find a Unix Domain Socket File to connect to the Discord client.
     *
     * @throws JDiscordIPCException.DiscordClientUnavailableException If a unix domain socket file could not be found.
     */
    private Path getIpcFilePath() throws JDiscordIPCException.DiscordClientUnavailableException {
        final Path temporaryDirectory = SystemUtil.getTemporaryDirectory();

        for (int i = 0; i <= 9; i++) {
            final Path ipcFile = temporaryDirectory.resolve("discord-ipc-" + i);
            if (Files.exists(ipcFile)) {
                return ipcFile;
            }
        }

        throw new JDiscordIPCException.DiscordClientUnavailableException(null);
    }
}
