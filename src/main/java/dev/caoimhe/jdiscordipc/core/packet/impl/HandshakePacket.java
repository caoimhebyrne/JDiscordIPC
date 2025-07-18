package dev.caoimhe.jdiscordipc.core.packet.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.caoimhe.jdiscordipc.core.packet.Packet;
import dev.caoimhe.jdiscordipc.core.packet.PacketOpcode;

/**
 * Sent to the Discord client upon establishing the connection.
 */
public class HandshakePacket implements Packet {
    // https://github.com/discord/discord-rpc/blob/master/src/rpc_connection.cpp#L6
    public static final int DEFAULT_VERSION = 1;

    @JsonProperty("v")
    private final int version;

    @JsonProperty("client_id")
    private final String clientId;

    /**
     * Initializes a new {@link HandshakePacket}.
     *
     * @param version The version of the protocol being used.
     * @param clientId The client ID of the application.
     */
    public HandshakePacket(final int version, final String clientId) {
        this.version = version;
        this.clientId = clientId;
    }

    /**
     * Initializes a new {@link HandshakePacket} using the {@link HandshakePacket#DEFAULT_VERSION}.
     *
     * @param clientId The client ID of the application.
     */
    public HandshakePacket(final long clientId) {
        this(HandshakePacket.DEFAULT_VERSION, String.valueOf(clientId));
    }

    @Override
    public String toString() {
        return "HandshakePacket { version = " + this.version + ", clientId = \"" + this.clientId + "\" }";
    }

    @Override
    public PacketOpcode opcode() {
        return PacketOpcode.HANDSHAKE;
    }
}
