package dev.caoimhe.jdiscordipc.modern.core;

import dev.caoimhe.jdiscordipc.core.SystemSocket;
import dev.caoimhe.jdiscordipc.core.SystemSocketFactory;

/**
 * A {@link SystemSocketFactory} implementation which returns instances of {@link ModernSystemSocket}.
 */
public class ModernSystemSocketFactory implements SystemSocketFactory {
    @Override
    public SystemSocket createSystemSocket() {
        return new ModernSystemSocket();
    }
}
