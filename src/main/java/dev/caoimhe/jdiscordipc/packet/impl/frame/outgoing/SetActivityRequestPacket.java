package dev.caoimhe.jdiscordipc.packet.impl.frame.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.caoimhe.jdiscordipc.packet.impl.frame.OutgoingFramePacket;
import dev.caoimhe.jdiscordipc.activity.model.Activity;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

/**
 * Sent to the Discord client as a request to update the user's activity.
 */
public class SetActivityRequestPacket extends OutgoingFramePacket<SetActivityRequestPacket.Arguments> {
    /**
     * Initializes a new {@link SetActivityRequestPacket} with {@link Arguments}.
     *
     * @param arguments The activity information to send to the Discord client.
     */
    public SetActivityRequestPacket(final Arguments arguments) {
        // SET_ACTIVITY packets require a unique ID to be set.
        super("SET_ACTIVITY", arguments, UUID.randomUUID().toString());
    }

    public static class Arguments {
        /**
         * The current process ID, used by Discord to know when to stop showing the activity.
         */
        @JsonProperty("pid")
        private final long processId;

        /**
         * The (optional) activity to set on the user. If null, the activity will be cleared for
         * the current process ID.
         */
        @JsonProperty("activity")
        private final @Nullable Activity activity;

        public Arguments(final long processId, final @Nullable Activity activity) {
            this.processId = processId;
            this.activity = activity;
        }
    }
}
