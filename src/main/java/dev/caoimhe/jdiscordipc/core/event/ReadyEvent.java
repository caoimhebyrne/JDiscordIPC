package dev.caoimhe.jdiscordipc.core.event;

/**
 * Received from the Discord client after it receives our {@link dev.caoimhe.jdiscordipc.core.packet.impl.HandshakePacket}.
 */
public class ReadyEvent implements Event {
    @Override
    public String toString() {
        return "ReadyEvent { }";
    }
}
