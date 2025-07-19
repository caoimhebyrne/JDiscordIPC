package dev.caoimhe.jdiscordipc.modern.socket;

import dev.caoimhe.jdiscordipc.socket.SystemSocket;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

/**
 * A {@link SystemSocket} implementation that communicates with the Discord client over a {@link SocketChannel}.
 */
public class ModernSystemSocket implements SystemSocket {
    private final SocketChannel socketChannel;

    public ModernSystemSocket() {
        try {
            this.socketChannel = SocketChannel.open(StandardProtocolFamily.UNIX);
        } catch (final IOException e) {
            // If we cannot open a simple server socket channel, there's something very wrong.
            throw new RuntimeException(e);
        }
    }

    @Override
    public void connect(final Path domainSocketPath) throws IOException {
        final UnixDomainSocketAddress unixDomainSocketAddress = UnixDomainSocketAddress.of(domainSocketPath);
        this.socketChannel.connect(unixDomainSocketAddress);
    }

    @Override
    public boolean readFully(final ByteBuffer byteBuffer) throws IOException {
        while (byteBuffer.hasRemaining()) {
            if (!this.socketChannel.isConnected()) {
                return false;
            }

            if (this.socketChannel.read(byteBuffer) == -1) {
                return false;
            }
        }

        byteBuffer.position(0);
        return true;
    }

    @Override
    public void write(final ByteBuffer byteBuffer) throws IOException {
        if (!this.socketChannel.isConnected()) {
            return;
        }

        byteBuffer.position(0);
        this.socketChannel.write(byteBuffer);
    }

    @Override
    public boolean isConnected() {
        return this.socketChannel.isConnected();
    }
}
