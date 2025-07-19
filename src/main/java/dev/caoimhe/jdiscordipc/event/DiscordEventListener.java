package dev.caoimhe.jdiscordipc.event;

import dev.caoimhe.jdiscordipc.event.model.Event;
import dev.caoimhe.jdiscordipc.event.model.ReadyEvent;

/**
 * An interface for all Discord event listeners to implement.
 *
 * @see Event
 */
public interface DiscordEventListener {
    /**
     * Fired when an event has been dispatched by the Discord client.
     *
     * @param event The dispatched event.
     */
    default void onEvent(final Event event) {
        if (event instanceof ReadyEvent) {
            this.onReadyEvent((ReadyEvent) event);
        }
    }

    /**
     * Fired when the Discord client indicates that it is ready.
     *
     * @param event The {@link ReadyEvent}.
     */
    default void onReadyEvent(final ReadyEvent event) {
    }
}
