package dev.caoimhe.jdiscordipc.packet;

/**
 * A message contained within a packet being sent to or read from the Discord client.
 */
public interface Packet {
    /**
     * The opcode for this packet.
     */
    PacketOpcode opcode();
}
