package dev.caoimhe.jdiscordipc.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * An interface to be implemented which handles communication between JDiscordIPC and the Discord application over a
 * Unix Domain Socket.
 */
public interface SystemSocket {
    /**
     * Connects this {@link SystemSocket} instance to the domain socket at the provided {@link Path}.
     *
     * @param domainSocketPath The location of the domain socket to connect to.
     *
     * @throws IOException If an error occurs while connecting to the socket.
     */
    void connect(final Path domainSocketPath) throws IOException;

    /**
     * Attempts to read bytes from the socket into the provided {@link ByteBuffer}.
     * <p></p>
     * If there is not enough bytes available to read into the buffer, this will block until the bytes become available.
     * The {@link ByteBuffer} must be array-backed for this operation to succeed.
     *
     * @param byteBuffer The byte buffer to read into.
     * @return Whether the operation was successful, if `false`, it's likely that the socket was closed.
     */
    boolean readFully(final ByteBuffer byteBuffer) throws IOException;

    /**
     * Writes a {@link ByteBuffer} to the socket.
     *
     * @param byteBuffer The byte buffer to write.
     */
    void write(final ByteBuffer byteBuffer) throws IOException;

    /**
     * Returns whether this {@link SystemSocket} instance is connected to the socket.
     */
    boolean isConnected();
}
