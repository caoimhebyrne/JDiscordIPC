package dev.caoimhe.jdiscordipc.event.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.caoimhe.jdiscordipc.packet.impl.HandshakePacket;

/**
 * Received from the Discord client when the client joins a voice channel.
 */
public class VoiceChannelSelectEvent implements Event {
    @JsonProperty("channel_id")
    private final String channelId;

    @JsonProperty("guild_id")
    private final String guildId;

    @JsonCreator
    public VoiceChannelSelectEvent(
        final @JsonProperty("channel_id") String channelId,
        final @JsonProperty("guild_id") String guildId
    ) {
        this.channelId = channelId;
        this.guildId = guildId;
    }

    @Override
    public String toString() {
        return "VoiceChannelSelectEvent { channel_id = " + this.channelId + ", guild_id = " + this.guildId + " }";
    }
}
