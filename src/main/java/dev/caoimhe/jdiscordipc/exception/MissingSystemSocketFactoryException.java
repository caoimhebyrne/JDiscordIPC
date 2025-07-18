package dev.caoimhe.jdiscordipc.exception;

public class MissingSystemSocketFactoryException extends RuntimeException {
    public MissingSystemSocketFactoryException() {
        super("JDiscordIPCBuilder.build() was called, but no `SystemSocketFactory` implementation was provided!");
    }
}
