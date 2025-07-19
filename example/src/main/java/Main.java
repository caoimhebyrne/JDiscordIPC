import dev.caoimhe.jdiscordipc.JDiscordIPC;
import dev.caoimhe.jdiscordipc.activity.model.Activity;
import dev.caoimhe.jdiscordipc.activity.model.party.ActivityPartyPrivacy;
import dev.caoimhe.jdiscordipc.exception.JDiscordIPCException;
import dev.caoimhe.jdiscordipc.modern.core.ModernSystemSocketFactory;

public class Main {
    public static void main(final String[] args) {
        final JDiscordIPC jDiscordIPC = JDiscordIPC.builder(945428344806183003L)
//            .systemSocketFactory(new LegacySystemSocketFactory())
            .systemSocketFactory(new ModernSystemSocketFactory())
            .build();

        final Activity activity = Activity.builder()
            // The activity details is the state of what the user is doing in the activity.
            .details("Selecting a game mode")
            // The activity state is the state of the party in the activity.
            .state("In lobby")
            .party("party-1", 2, (builder) -> {
                builder.maximumSize(10);
                builder.privacy(ActivityPartyPrivacy.PUBLIC);
            })
            .build();

        // If an activity is set before JDiscordIPC is connected, it'll queue the activity to be set once a ready
        // event is received from Discord.
        jDiscordIPC.updateActivity(activity);

        // This will attempt to initialize the connection between JDiscordIPC and the Discord client.
        // This will block for a short amount of time, but not for too long. If you wish to know when the Discord client
        // is *completely ready*, check out the `DiscordEventListener` and `JDiscordIPC#registerEventListener`.
        try {
            jDiscordIPC.connect();
        } catch (final JDiscordIPCException.DiscordClientUnavailableException e) {
            System.err.println("Failed to connect to a Discord client. Is the discord application open?");
        }
    }
}
