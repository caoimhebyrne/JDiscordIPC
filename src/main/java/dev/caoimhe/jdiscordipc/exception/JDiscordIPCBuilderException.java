package dev.caoimhe.jdiscordipc.exception;

import org.jspecify.annotations.Nullable;

/**
 * A base class for all exceptions that can be thrown within a {@link dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder} to implement.
 */
public class JDiscordIPCBuilderException extends RuntimeException {
    private JDiscordIPCBuilderException(final String message, final @Nullable Throwable cause) {
        super(message, cause);
    }

    /**
     * Thrown when {@link dev.caoimhe.jdiscordipc.builder.JDiscordIPCBuilder#build} is called without specifying
     * a system socket factory.
     */
    public static class MissingSystemSocketFactoryException extends JDiscordIPCBuilderException {
        public MissingSystemSocketFactoryException() {
            super("JDiscordIPCBuilder.build() was called, but no `SystemSocketFactory` implementation was provided!", null);
        }
    }
}
