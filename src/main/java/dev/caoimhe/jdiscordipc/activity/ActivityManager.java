package dev.caoimhe.jdiscordipc.activity;

import dev.caoimhe.jdiscordipc.JDiscordIPC;
import dev.caoimhe.jdiscordipc.JDiscordIPCState;
import dev.caoimhe.jdiscordipc.activity.model.Activity;
import dev.caoimhe.jdiscordipc.activity.model.ActivityBuilder;
import dev.caoimhe.jdiscordipc.event.DiscordEventListener;
import dev.caoimhe.jdiscordipc.event.model.ReadyEvent;
import dev.caoimhe.jdiscordipc.packet.PacketManager;
import dev.caoimhe.jdiscordipc.packet.impl.frame.outgoing.SetActivityRequestPacket;
import dev.caoimhe.jdiscordipc.util.SystemUtil;
import org.jspecify.annotations.Nullable;

/**
 * Responsible for setting the current user's activity once {@link ReadyEvent} is emitted.
 */
public class ActivityManager implements DiscordEventListener {
    /**
     * The {@link JDiscordIPC} instance that this manager is for.
     */
    private final JDiscordIPC jDiscordIPC;

    /**
     * The {@link PacketManager} instance to use for sending packets.
     */
    private final PacketManager packetManager;

    /**
     * The current user's latest activity state.
     * <p>
     * When {@link ReadyEvent} is emitted, this will be set. This ensures that {@link #updateActivity} calls can be
     * queued, and also ensures that the user's activity is restored when reconnecting to Discord.
     */
    private @Nullable Activity currentActivity;

    /**
     * Initializes a new {@link ActivityManager} instance.
     *
     * @param jDiscordIPC   The {@link JDiscordIPC} instance that this manager is for.
     * @param packetManager The {@link PacketManager} instance to send packets with.
     */
    public ActivityManager(final JDiscordIPC jDiscordIPC, final PacketManager packetManager) {
        this.currentActivity = null;
        this.packetManager = packetManager;

        this.jDiscordIPC = jDiscordIPC;
        this.jDiscordIPC.registerEventListener(this);
    }

    /**
     * Queues an activity update for the current user.
     * <p>
     * If {@link JDiscordIPC#connect()} has not been called, this will queue the {@link Activity} to be set once the
     * Discord client has connected (i.e. once {@link ReadyEvent} is dispatched).
     *
     * @param activity The activity to set on the user's profile. If null, the current activity belonging to this
     *                 application instance will be removed from the user's profile
     * @see Activity
     * @see ActivityBuilder
     */
    public void updateActivity(final @Nullable Activity activity) {
        this.currentActivity = activity;

        // If the JDiscordIPC instance is not in a ready state, we don't need to do anything else, the activity will be
        // set once it reaches the ready state.
        if (this.jDiscordIPC.state() != JDiscordIPCState.READY) return;

        // The client is currently in a ready state, we can send the activity packet now.
        this.sendActivityPacket();
    }

    @Override
    public void onReadyEvent(final ReadyEvent event) {
        // When the Discord client informs us that it is ready for communication, we can set the user's current
        // activity to the latest cached one.
        this.sendActivityPacket();
    }

    /**
     * Sends the activity for the current user as a packet to the Discord client.
     */
    private void sendActivityPacket() {
        this.packetManager.sendPacket(new SetActivityRequestPacket(new SetActivityRequestPacket.Arguments(
            SystemUtil.getProcessId(),
            this.currentActivity
        )));
    }
}
