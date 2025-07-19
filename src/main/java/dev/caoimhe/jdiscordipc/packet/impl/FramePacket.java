package dev.caoimhe.jdiscordipc.packet.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.caoimhe.jdiscordipc.packet.Packet;
import dev.caoimhe.jdiscordipc.packet.PacketOpcode;
import org.jspecify.annotations.Nullable;

/**
 * A base class for all frame packets to implement, most packets within the protocol are a form of frame packet.
 */
public abstract class FramePacket implements Packet {
    /**
     * The command for this frame packet, used as an indicator for the actual data within the packet.
     */
    @JsonProperty("cmd")
    protected String command;

    /**
     * The unique ID for this frame packet.
     */
    @JsonProperty("nonce")
    protected @Nullable String uniqueId;

    /**
     * The command for this frame packet, used as an indicator for the actual data within the packet.
     */
    public String command() {
        return this.command;
    }

    /**
     * The unique ID for this frame packet.
     */
    public @Nullable String uniqueId() {
        return this.uniqueId;
    }

    /**
     * Sets the unique ID for this frame packet.
     */
    public void setUniqueId(final String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public PacketOpcode opcode() {
        return PacketOpcode.FRAME;
    }
}
