package dev.caoimhe.jdiscordipc.packet;

import dev.caoimhe.jdiscordipc.packet.codec.PacketCodec;
import dev.caoimhe.jdiscordipc.socket.SystemSocket;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

/**
 * Responsible for sending packets to and reading packets from a {@link SystemSocket}.
 */
public class PacketManager {
    /**
     * The {@link PacketCodec} to read and write packets to/from.
     */
    private final PacketCodec codec;

    /**
     * The {@link PacketHandler} instance to dispatch incoming packets to.
     */
    private final PacketHandler packetHandler;

    /**
     * The {@link Thread} used to read incoming packets.
     */
    private @Nullable Thread packetReadingThread;

    /**
     * The {@link SystemSocket} used for the {@link PacketCodec}.
     */
    private final SystemSocket systemSocket;

    /**
     * Initializes a new {@link PacketManager}.
     *
     * @param systemSocket The system socket to read to/write from.
     */
    public PacketManager(final PacketHandler packetHandler, final SystemSocket systemSocket) {
        this.codec = PacketCodec.from(systemSocket);
        this.packetHandler = packetHandler;
        this.packetReadingThread = null;
        this.systemSocket = systemSocket;
    }

    /**
     * Sends a packet to the Discord client.
     * <p>
     * This method may block until all bytes are written to the underlying socket. If an exception occurs when attempting
     * to write to the underlying socket, this method will do nothing.
     */
    public void sendPacket(final Packet packet) {
        try {
            this.codec.write(packet);
        } catch (final IOException ignored) {
        }
    }

    /**
     * Starts reading incoming packets from the Discord client.
     * <p>
     * If a thread was previously started for this (i.e. this method was called before), it will be interrupted and
     * replaced.
     *
     * @see #stopReadingIncomingPackets()
     */
    public void startReadingIncomingPackets() {
        // If an existing thread is already running, let's get rid of it and start a new one.
        this.stopReadingIncomingPackets();

        // Now that we've connected, we can start a background task to start consuming messages from Discord.
        this.packetReadingThread = new Thread(this::readPackets, "JDiscordIPC-Packet-Reading");
        this.packetReadingThread.setDaemon(false);
        this.packetReadingThread.start();
    }

    /**
     * Stops the packet reading thread if it is not already stopped.
     *
     * @see #startReadingIncomingPackets()
     */
    public void stopReadingIncomingPackets() {
        if (this.packetReadingThread != null) {
            this.packetReadingThread.interrupt();
            this.packetReadingThread = null;
        }
    }

    /**
     * Attempts to read packets from the Discord client, handing them off to the registered {@link PacketHandler}.
     */
    private void readPackets() {
        while (this.systemSocket.isConnected()) {
            // If the current thread has been interrupted, we can just return immediately.
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            final Packet packet;
            try {
                // This will block until a packet is read, or return null when EOF is reached.
                packet = this.codec.read();
            } catch (final Exception e) {
                System.err.println("Failed to read incoming packet: " + e);
                continue;
            }

            // If the read packet is null, the end of the stream was reached.
            if (packet == null) {
                this.packetHandler.handleEOF();
                return;
            }

            try {
                this.packetHandler.handlePacket(packet);
            } catch (final Exception e) {
                System.err.println("Failed to handle packet: " + e);
            }
        }
    }
}
