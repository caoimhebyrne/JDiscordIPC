package dev.caoimhe.jdiscordipc.core.packet.impl;

import dev.caoimhe.jdiscordipc.core.packet.Packet;
import dev.caoimhe.jdiscordipc.core.packet.PacketOpcode;

/**
 * Received when the Discord client closes the connection, typically due to an invalid request.
 */
public class ClosePacket implements Packet {
    private final int code;
    private final String message;

    private ClosePacket(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * The code (reason) that the connection was closed.
     */
    public int code() {
        return this.code;
    }

    /**
     * A text reason for the connection closure.
     */
    public String message() {
        return this.message;
    }

    @Override
    public String toString() {
        return "ClosePacket { code = " + this.code + ", message = \"" + this.message + "\" }";
    }

    @Override
    public PacketOpcode opcode() {
        return PacketOpcode.CLOSE;
    }
}
