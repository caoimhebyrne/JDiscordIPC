import dev.caoimhe.jdiscordipc.JDiscordIPC;
import dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder;
import dev.caoimhe.jdiscordipc.event.DiscordEventListener;
import dev.caoimhe.jdiscordipc.exception.JDiscordIPCException;
import dev.caoimhe.jdiscordipc.model.activity.Activity;
import dev.caoimhe.jdiscordipc.model.activity.party.ActivityPartyPrivacy;
import dev.caoimhe.jdiscordipc.model.event.ReadyEvent;
import dev.caoimhe.jdiscordipc.modern.core.ModernSystemSocketFactory;

public class Main {
    public static void main(final String[] args) {
        final JDiscordIPC jDiscordIPC = JDiscordIPCBuilder.of(945428344806183003L)
            .systemSocketFactory(new ModernSystemSocketFactory())
            .build();

        jDiscordIPC.registerEventListener(new DiscordEventListener() {
            @Override
            public void onReadyEvent(final ReadyEvent event) {
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

                jDiscordIPC.updateActivity(activity);
            }
        });

        try {
            jDiscordIPC.connect();
        } catch (final JDiscordIPCException.DiscordClientUnavailableException e) {
            System.err.println("Failed to connect to a Discord client. Is the discord application open?");
        }
    }
}
