package dev.caoimhe.jdiscordipc.socket.impl;

import dev.caoimhe.jdiscordipc.socket.SystemSocket;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * A {@link SystemSocket} implementation that communicates with the Discord client over a {@link RandomAccessFile}.
 */
public class WindowsSystemSocket implements SystemSocket {
    private @Nullable RandomAccessFile randomAccessFile;

    /**
     * Initializes a new {@link WindowsSystemSocket}.
     */
    public WindowsSystemSocket() {
        this.randomAccessFile = null;
    }

    @Override
    public void connect(final Path domainSocketPath) throws IOException {
        this.randomAccessFile = new RandomAccessFile(domainSocketPath.toFile(), "rw");
    }

    @Override
    public boolean readFully(final ByteBuffer byteBuffer) throws IOException {
        if (this.randomAccessFile == null) {
            return false;
        }

        final FileChannel channel = this.randomAccessFile.getChannel();
        while (byteBuffer.hasRemaining()) {
            if (channel.read(byteBuffer) == -1) {
                return false;
            }
        }

        byteBuffer.position(0);
        return true;
    }

    @Override
    public void write(final ByteBuffer byteBuffer) throws IOException {
        if (this.randomAccessFile == null) {
            return;
        }

        this.randomAccessFile.write(byteBuffer.array());
    }

    @Override
    public boolean isConnected() {
        return this.randomAccessFile != null;
    }
}
