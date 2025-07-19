package dev.caoimhe.jdiscordipc.packet.impl;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.caoimhe.jdiscordipc.packet.Packet;
import dev.caoimhe.jdiscordipc.packet.PacketOpcode;

import java.util.HashMap;
import java.util.Map;

/**
 * A packet sent to or received from the Discord client as part of an echo command (see {@link PingPacket}).
 * <p>
 * This should be received after sending a {@link PingPacket}, or sent after receiving a {@link PingPacket}.
 *
 * @see PingPacket
 */
public class PongPacket implements Packet {
    /**
     * The properties contained within this pong packet.
     */
    @JsonIgnore
    private final Map<String, Object> properties;

    /**
     * Initializes a new {@link PongPacket} with an empty map for its properties.
     */
    @JsonCreator
    public PongPacket() {
        this.properties = new HashMap<>();
    }

    /**
     * Initializes a new {@link PongPacket} with some properties.
     *
     * @param properties The properties contained within this pong packet.
     */
    public PongPacket(final Map<String, Object> properties) {
        this.properties = new HashMap<>(properties);
    }

    @JsonAnySetter
    public void setProperties(final String key, final Object value) {
        this.properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> properties() {
        return this.properties;
    }

    @Override
    public PacketOpcode opcode() {
        return PacketOpcode.PONG;
    }
}
