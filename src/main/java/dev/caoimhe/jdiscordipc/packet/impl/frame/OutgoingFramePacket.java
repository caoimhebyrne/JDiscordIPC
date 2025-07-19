package dev.caoimhe.jdiscordipc.packet.impl.frame;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.caoimhe.jdiscordipc.packet.impl.FramePacket;
import org.jspecify.annotations.Nullable;

/**
 * A frame packet being sent to the Discord client.
 */
public class OutgoingFramePacket<A> extends FramePacket {
    @JsonProperty("args")
    private final A arguments;

    /**
     * Initializes a new {@link OutgoingFramePacket}
     */
    public OutgoingFramePacket(final String command, final A arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    /**
     * The arguments contained within this outgoing frame packet.
     */
    public A arguments() {
        return this.arguments;
    }
}
