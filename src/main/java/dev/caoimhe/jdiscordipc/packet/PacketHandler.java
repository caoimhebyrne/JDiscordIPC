package dev.caoimhe.jdiscordipc.packet;

/**
 * Responsible for handling incoming packets, and dealing with any errors that occur during the packet handling process.
 */
public interface PacketHandler {
    /**
     * Called when a packet is received from the Discord client.
     */
    void handlePacket(final Packet packet);

    /**
     * Called the end of the underlying socket is reached, i.e. the connection was forcefully terminated.
     */
    void handleEOF();
}
