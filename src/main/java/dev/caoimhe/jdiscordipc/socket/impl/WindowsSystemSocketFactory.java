package dev.caoimhe.jdiscordipc.socket.impl;

import dev.caoimhe.jdiscordipc.socket.SystemSocket;
import dev.caoimhe.jdiscordipc.socket.SystemSocketFactory;

/**
 * A {@link SystemSocketFactory} implementation which returns instances of {@link WindowsSystemSocket}.
 */
public class WindowsSystemSocketFactory implements SystemSocketFactory {
    @Override
    public SystemSocket createSystemSocket() {
        return new WindowsSystemSocket();
    }
}
