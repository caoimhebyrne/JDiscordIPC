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
 * A packet sent to or received from the Discord client as part of an echo command (see {@link PongPacket}).
 * <ul>
 * <li>If this is sent, the Discord client should respond with a {@link PongPacket} with the same {@link #properties}.</li>
 * <li>If this is received, a {@link PongPacket} should be sent with the same {@link #properties}.</li>
 * </ul>
 */
public class PingPacket implements Packet {
    /**
     * The properties contained within this ping packet.
     */
    @JsonIgnore
    private final Map<String, Object> properties;

    @JsonCreator
    public PingPacket() {
        this.properties = new HashMap<>();
    }

    public PingPacket(final Map<String, Object> properties) {
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
        return PacketOpcode.PING;
    }
}
