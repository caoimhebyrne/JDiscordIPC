package dev.caoimhe.jdiscordipc.modern.socket;

import dev.caoimhe.jdiscordipc.socket.SystemSocket;
import dev.caoimhe.jdiscordipc.socket.SystemSocketFactory;

/**
 * A {@link SystemSocketFactory} implementation which returns instances of {@link ModernSystemSocket}.
 */
public class ModernSystemSocketFactory implements SystemSocketFactory {
    @Override
    public SystemSocket createSystemSocket() {
        return new ModernSystemSocket();
    }
}
