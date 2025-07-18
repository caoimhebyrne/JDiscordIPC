import dev.caoimhe.jdiscordipc.JDiscordIPC;
import dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder;
import dev.caoimhe.jdiscordipc.exception.DiscordClientUnavailableException;
import dev.caoimhe.jdiscordipc.modern.core.ModernSystemSocketFactory;

public class Main {
    public static void main(final String[] args) {
        final JDiscordIPC jDiscordIPC = JDiscordIPCBuilder.of(945428344806183003L)
            .systemSocketFactory(new ModernSystemSocketFactory())
            .build();

        try {
            jDiscordIPC.connect();
        } catch (final DiscordClientUnavailableException e) {
            System.err.println("Failed to connect to a Discord client. Is the discord application open?");
        }
    }
}
