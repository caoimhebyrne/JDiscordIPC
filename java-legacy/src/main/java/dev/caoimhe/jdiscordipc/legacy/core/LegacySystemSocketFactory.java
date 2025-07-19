package dev.caoimhe.jdiscordipc.legacy.core;

import dev.caoimhe.jdiscordipc.core.SystemSocket;
import dev.caoimhe.jdiscordipc.core.SystemSocketFactory;

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
