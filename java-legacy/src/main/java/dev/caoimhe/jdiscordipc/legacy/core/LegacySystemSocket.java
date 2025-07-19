package dev.caoimhe.jdiscordipc.legacy.core;

import dev.caoimhe.jdiscordipc.core.SystemSocket;
import org.jspecify.annotations.Nullable;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * A {@link SystemSocket} which communicates with the Discord client over an {@link AFUNIXSocket}.
 */
public class LegacySystemSocket implements SystemSocket {
    private final AFUNIXSocket socket;
    private @Nullable DataInputStream dataInputStream;

    /**
     * Initializes a new {@link LegacySystemSocket} instance.
     *
     * @throws IOException If an error occurs when initializing the socket.
     */
    public LegacySystemSocket() throws IOException {
        this.socket = AFUNIXSocket.newInstance();
    }

    @Override
    public void connect(final Path domainSocketPath) throws IOException {
        final AFUNIXSocketAddress socketAddress = AFUNIXSocketAddress.of(domainSocketPath);
        this.socket.connect(socketAddress);
        this.dataInputStream = new DataInputStream(this.socket.getInputStream());
    }

    @Override
    public boolean readFully(final ByteBuffer byteBuffer) throws IOException {
        // If the socket is not currently connected, we cannot read anything.
        if (!this.socket.isConnected() || this.dataInputStream == null) {
            return false;
        }

        try {
            // We do not need to close this DataInputStream, it's just a helpful wrapper around an existing input stream.
            this.dataInputStream.readFully(byteBuffer.array());
        } catch (final EOFException e) {
            return false;
        }

        return true;
    }

    @Override
    public void write(final ByteBuffer byteBuffer) throws IOException {
        if (!this.socket.isConnected()) {
            return;
        }

        byteBuffer.position(0);
        this.socket.getOutputStream().write(byteBuffer.array());
    }

    @Override
    public boolean isConnected() {
        return this.socket.isConnected();
    }
}
