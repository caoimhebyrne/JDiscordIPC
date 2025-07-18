package dev.caoimhe.jdiscordipc.core.packet.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.caoimhe.jdiscordipc.core.packet.Packet;
import dev.caoimhe.jdiscordipc.core.packet.PacketOpcode;
import dev.caoimhe.jdiscordipc.core.packet.impl.frame.DispatchEventPacket;
import org.jspecify.annotations.Nullable;

/**
 * A base class for all frame packets to implement, most packets within the protocol are a form of frame packet.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "cmd")
@JsonSubTypes(
    @JsonSubTypes.Type(value = DispatchEventPacket.class, name = "DISPATCH")
)
public abstract class FramePacket<D> implements Packet {
    /** The command for this frame packet, used as an indicator for the actual data within the packet. */
    @JsonProperty("cmd")
    private String command;

    /** The unique ID for this frame packet. */
    @JsonProperty("nonce")
    private @Nullable String uniqueId;

    /**
     * The command for this frame packet, used as an indicator for the actual data within the packet.
     */
    public String command() {
        return this.command;
    }

    /**
     * The data within this frame packet.
     */
    public abstract D data();

    /**
     * The unique ID for this frame packet.
     */
    public @Nullable String uniqueId() {
        return this.uniqueId;
    }

    @Override
    public PacketOpcode opcode() {
        return PacketOpcode.FRAME;
    }
}
