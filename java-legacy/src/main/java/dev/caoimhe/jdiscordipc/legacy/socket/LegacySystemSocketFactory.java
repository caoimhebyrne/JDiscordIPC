package dev.caoimhe.jdiscordipc.legacy.socket;

import dev.caoimhe.jdiscordipc.socket.SystemSocket;
import dev.caoimhe.jdiscordipc.socket.SystemSocketFactory;

import java.io.IOException;

/**
 * A {@link SystemSocketFactory} implementation which returns instances of {@link LegacySystemSocket}.
 */
public class LegacySystemSocketFactory implements SystemSocketFactory {
    @Override
    public SystemSocket createSystemSocket() {
        try {
            return new LegacySystemSocket();
        } catch (final IOException e) {
            // TODO: Is this the best thing we can do here? Probabyl.
            throw new RuntimeException(e);
        }
    }
}
