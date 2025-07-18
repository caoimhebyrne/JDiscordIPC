package dev.caoimhe.jdiscordipc;

import dev.caoimhe.jdiscordipc.core.SystemSocket;
import dev.caoimhe.jdiscordipc.core.codec.PacketCodec;
import dev.caoimhe.jdiscordipc.core.packet.Packet;
import dev.caoimhe.jdiscordipc.core.packet.impl.HandshakePacket;
import dev.caoimhe.jdiscordipc.core.packet.impl.frame.DispatchEventPacket;
import dev.caoimhe.jdiscordipc.event.DiscordEventListener;
import dev.caoimhe.jdiscordipc.exception.JDiscordIPCException;
import dev.caoimhe.jdiscordipc.model.event.Event;
import dev.caoimhe.jdiscordipc.util.SystemUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * The main entrypoint for JDiscordIPC.
 * It's recommended to only have one instance of {@link JDiscordIPC} per-application. Having multiple concurrent
 * instances may lead to undefined behavior.
 *
 * @see dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder
 */
public class JDiscordIPC {
    private final long clientId;
    private final ExecutorService executorService;
    private final List<DiscordEventListener> eventListeners;
    private final PacketCodec packetCodec;
    private final SystemSocket systemSocket;

    /**
     * Initializes a new {@link JDiscordIPC} instance.
     *
     * @param clientId        The client ID to use when communicating with Discord.
     * @param executorService The executor service to use to start background tasks, e.g. reading from the socket.
     * @param systemSocket    The system socket to read messages from and send messages to.
     * @see dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder
     */
    public JDiscordIPC(final long clientId, final ExecutorService executorService, final SystemSocket systemSocket) {
        this.clientId = clientId;
        this.executorService = executorService;
        this.eventListeners = new ArrayList<>();
        this.packetCodec = PacketCodec.from(systemSocket);
        this.systemSocket = systemSocket;
    }

    /**
     * Connects to the running Discord application through the {@link SystemSocket} provided during initialization.
     * This method will block until the connection is initiated, but not until it is ready (see {@link dev.caoimhe.jdiscordipc.model.event.ReadyEvent}).
     *
     * @throws JDiscordIPCException.DiscordClientUnavailableException When the connection could not be initiated.
     */
    public void connect() throws JDiscordIPCException.DiscordClientUnavailableException {
        final Path discordIpcPath = this.getIpcFilePath();

        try {
            this.systemSocket.connect(discordIpcPath);
        } catch (final IOException e) {
            throw new JDiscordIPCException.DiscordClientUnavailableException(e);
        }

        // Now that we've connected, we can start a background task to start consuming messages from Discord.
        this.executorService.execute(this::readPackets);

        // To mark the connection as ready, we must send a handshake to the client containing our client ID.
        try {
            this.packetCodec.write(new HandshakePacket(this.clientId));
        } catch (final IOException e) {
            throw new JDiscordIPCException.DiscordClientUnavailableException(e);
        }
    }

    /**
     * Registers an event listener with this {@link JDiscordIPC} instance.
     * <p>
     * In order to ensure that your listener receives all events, this should be called before {@link #connect}.
     * Otherwise, events like {@link dev.caoimhe.jdiscordipc.model.event.ReadyEvent} may be missed.
     *
     * @param listener The event listener instance to register.
     */
    public void registerEventListener(final DiscordEventListener listener) {
        this.eventListeners.add(listener);
    }

    /**
     * Responsible for reading packets from the Discord client and ensuring they get handled.
     */
    private void readPackets() {
        while (this.systemSocket.isConnected()) {
            final Packet packet;
            try {
                packet = this.packetCodec.read();
            } catch (final Exception e) {
                System.err.println("Failed to read packet: " + e);
                continue;
            }

            // If a packet could not be read, it's likely that we're not ready to read yet.
            if (packet == null) continue;

            try {
                this.handlePacket(packet);
            } catch (final Exception e) {
                System.err.println("Failed to handle packet: " + e);
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
        if (packet instanceof DispatchEventPacket) {
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
