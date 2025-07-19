# JDiscordIPC

> **Note**: JDiscordIPC is in early development. Expect breaking API changes, bugs, and more.

JDiscordIPC provides a safe and easy to use API for interacting with a running Discord client over the IPC protocol
(also known as the Game or Social SDK).

Unlike the Discord GameSDK, JDiscordIPC does not use the Discord-provided native library. Instead, it uses the system's
IPC protocol either through Java Sockets or junixsocket, depending on the platform you're using.

For more information about the Discord IPC protocol, check
out [Discord's developer documentation](https://discord.com/developers/docs/intro).

## Examples and Wiki

> **Note**: A wiki for JDiscordIPC is coming soon. See an example snippet below, or, check out
> the [example project](example/src/main/java/Main.java).

```java
class Main {
    public static void main(final String[] args) {
        final JDiscordIPC jDiscordIPC = JDiscordIPC.builder(/* clientId */)
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

        try {
            jDiscordIPC.connect();
        } catch (final JDiscordIPCException.DiscordClientUnavailableException e) {
            System.err.println("Failed to connect to a Discord client.");
        }
    }
}
```

## Using JDiscordIPC

> **Note**: These artifacts are not published to any repositories yet!

To use JDiscordIPC in your project, you need to add the core dependency:

```kotlin
dependencies {
    implementation("dev.caoimhe:jdiscordipc:0.1.0")
}
```

Alongside the dependency to provide the Unix Socket implementation, which depends on the Java version you're using:

- `java-legacy`: For targets less than Java 15. Uses the `junixsocket` library to communicate with the Discord IPC
  server.
- `java-modern`: For targets higher than Java 16. Uses Java Unix Domain Socket channels to communicate with the Discord
  IPC server.

```kotlin
dependencies {
    // Java 15 and lower:
    implementation("dev.caoimhe.jdiscordipc-java-legacy:0.1.0")

    // Java 16 and higher:
    implementation("dev.caoimhe.jdiscordipc-java-modern:0.1.0")
}
```

## JDiscordIPC vs KDiscordIPC

JDiscordIPC is a successor to KDiscordIPC. The main improvements of the Java version over the Kotlin version are:

- Not requiring the Kotlin standard library, which can inflate build size if you're not using Kotlin.
- Platform-agnostic core with separate implementations for communication via the system's socket protocol
- Better error handling and an easy-to-use API.

## License

This project uses the [MIT license](./LICENSE).
