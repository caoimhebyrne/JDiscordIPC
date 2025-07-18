package dev.caoimhe.jdiscordipc.modern.core;

import dev.caoimhe.jdiscordipc.core.SystemSocket;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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

            this.socketChannel.read(byteBuffer);
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
