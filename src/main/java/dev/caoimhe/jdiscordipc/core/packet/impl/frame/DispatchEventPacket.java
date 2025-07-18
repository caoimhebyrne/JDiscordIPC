package dev.caoimhe.jdiscordipc.core.packet.impl.frame;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.caoimhe.jdiscordipc.core.event.Event;
import dev.caoimhe.jdiscordipc.core.event.ReadyEvent;
import dev.caoimhe.jdiscordipc.core.packet.impl.FramePacket;

public class DispatchEventPacket extends FramePacket<Event> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "evt", visible = true)
    @JsonSubTypes(
        @JsonSubTypes.Type(value = ReadyEvent.class, name = "READY")
    )
    @JsonProperty("data")
    private Event data;

    @Override
    public Event data() {
        return this.data;
    }

    @Override
    public String toString() {
        return "DispatchEventPacket { event = " + this.data + " }";
    }
}
