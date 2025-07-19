package dev.caoimhe.jdiscordipc.core.packet.impl.frame.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.caoimhe.jdiscordipc.core.packet.impl.frame.IncomingFramePacket;
import dev.caoimhe.jdiscordipc.event.model.Event;
import dev.caoimhe.jdiscordipc.event.model.ReadyEvent;

public class DispatchEventPacket extends IncomingFramePacket<Event> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "evt", visible = true)
    @JsonSubTypes(
        @JsonSubTypes.Type(value = ReadyEvent.class, name = "READY")
    )
    @JsonProperty("data")
    protected Event data;

    @Override
    public Event data() {
        return this.data;
    }

    @Override
    public String toString() {
        return "DispatchEventPacket { event = " + this.data + " }";
    }
}
