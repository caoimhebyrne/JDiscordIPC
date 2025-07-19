package dev.caoimhe.jdiscordipc.packet.impl.frame;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.caoimhe.jdiscordipc.packet.impl.FramePacket;
import dev.caoimhe.jdiscordipc.packet.impl.frame.incoming.DispatchEventPacket;
import dev.caoimhe.jdiscordipc.packet.impl.frame.incoming.SetActivityResponsePacket;

/**
 * A frame packet being received from the Discord client.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "cmd")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DispatchEventPacket.class, name = "DISPATCH"),
    @JsonSubTypes.Type(value = SetActivityResponsePacket.class, name = "SET_ACTIVITY"),
})
public abstract class IncomingFramePacket<D> extends FramePacket {
    /**
     * Returns the data contained within this incoming frame packet.
     */
    public abstract D data();
}
