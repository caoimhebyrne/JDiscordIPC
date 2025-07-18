package dev.caoimhe.jdiscordipc;

import dev.caoimhe.jdiscordipc.core.SystemSocket;
import dev.caoimhe.jdiscordipc.core.codec.PacketCodec;
import dev.caoimhe.jdiscordipc.core.packet.Packet;
import dev.caoimhe.jdiscordipc.core.packet.impl.HandshakePacket;
import dev.caoimhe.jdiscordipc.exception.DiscordClientUnavailableException;
import dev.caoimhe.jdiscordipc.util.SystemUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private final PacketCodec packetCodec;
    private final SystemSocket systemSocket;

    /**
     * Initializes a new {@link JDiscordIPC} instance.
     *
     * @param clientId The client ID to use when communicating with Discord.
     * @param executorService The executor service to use to start background tasks, e.g. reading from the socket.
     * @param systemSocket The system socket to read messages from and send messages to.
     *
     * @see dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder
     */
    public JDiscordIPC(final long clientId, final ExecutorService executorService, final SystemSocket systemSocket) {
        this.clientId = clientId;
        this.executorService = executorService;
        this.packetCodec = PacketCodec.from(systemSocket);
        this.systemSocket = systemSocket;
    }

    /**
     * Connects to the running Discord application through the {@link SystemSocket} provided during initialization.
     */
    public void connect() throws DiscordClientUnavailableException {
        final Path discordIpcPath = this.getIpcFilePath();

        try {
            this.systemSocket.connect(discordIpcPath);
        } catch (final IOException e) {
            throw new DiscordClientUnavailableException(e);
        }

        // Now that we've connected, we can start a background task to start consuming messages from Discord.
        this.executorService.execute(() -> {
            while (this.systemSocket.isConnected()) {
                try {
                    final Packet packet = this.packetCodec.read();
                    System.out.println(packet);
                } catch (final Exception e) {
                    System.err.println("Failed to read packet: " + e);
                }
            }
        });

        try {
            this.packetCodec.write(new HandshakePacket(this.clientId));
        } catch (final IOException e) {
            System.err.println("Failed to write packet to socket: " + e);
        }
    }

    /**
     * Attempts to find a Unix Domain Socket File to connect to the Discord client.
     *
     * @throws DiscordClientUnavailableException If a unix domain socket file could not be found.
     */
    private Path getIpcFilePath() throws DiscordClientUnavailableException {
        final Path temporaryDirectory = SystemUtil.getTemporaryDirectory();

        for (int i = 0; i <= 9; i++) {
            final Path ipcFile = temporaryDirectory.resolve("discord-ipc-" + i);
            if (Files.exists(ipcFile)) {
                return ipcFile;
            }
        }

        throw new DiscordClientUnavailableException(null);
    }
}
