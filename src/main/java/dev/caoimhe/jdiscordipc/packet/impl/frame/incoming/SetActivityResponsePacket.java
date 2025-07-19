package dev.caoimhe.jdiscordipc.packet.impl.frame.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.caoimhe.jdiscordipc.activity.model.Activity;
import dev.caoimhe.jdiscordipc.packet.impl.frame.IncomingFramePacket;

/**
 * Received from the Discord client as a response to {@link dev.caoimhe.jdiscordipc.packet.impl.frame.outgoing.SetActivityRequestPacket}.
 */
public class SetActivityResponsePacket extends IncomingFramePacket<Activity> {
    @JsonProperty("data")
    private Activity data;

    @Override
    public Activity data() {
        return this.data;
    }
}
