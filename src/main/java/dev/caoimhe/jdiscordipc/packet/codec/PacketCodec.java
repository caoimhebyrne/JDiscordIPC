package dev.caoimhe.jdiscordipc.packet.codec;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.caoimhe.jdiscordipc.packet.Packet;
import dev.caoimhe.jdiscordipc.packet.PacketOpcode;
import dev.caoimhe.jdiscordipc.packet.impl.ClosePacket;
import dev.caoimhe.jdiscordipc.packet.impl.PingPacket;
import dev.caoimhe.jdiscordipc.packet.impl.PongPacket;
import dev.caoimhe.jdiscordipc.packet.impl.frame.IncomingFramePacket;
import dev.caoimhe.jdiscordipc.socket.SystemSocket;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * Responsible for encoding and decoding bytes to and from packets.
 */
public class PacketCodec {
    // Two 32-bit integers, one for the opcode and one for the payload size.
    private static final int HEADER_SIZE_BYTES = 8;

    /**
     * A temporary buffer used to read a packet's header.
     */
    private final ByteBuffer headerBuffer;

    /**
     * The {@link JsonMapper} to use when (de)serializing JSON values.
     */
    private final JsonMapper jsonMapper;

    private final ReadFunction readFunction;
    private final WriteFunction writeFunction;

    /**
     * Initializes a new {@link PacketCodec} instance.
     *
     * @param readFunction  The function to call when reading bytes.
     * @param writeFunction The function to call when writing bytes.
     */
    public PacketCodec(final ReadFunction readFunction, final WriteFunction writeFunction) {
        // Discord uses little endian for the integers stored within the packet's header.
        this.headerBuffer = ByteBuffer.allocate(HEADER_SIZE_BYTES);
        this.headerBuffer.order(ByteOrder.LITTLE_ENDIAN);

        this.jsonMapper = JsonMapper.builder()
            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .changeDefaultPropertyInclusion(value -> value.withValueInclusion(JsonInclude.Include.NON_NULL))
            .changeDefaultVisibility(value -> value.withFieldVisibility(JsonAutoDetect.Visibility.ANY))
            .build();

        this.readFunction = readFunction;
        this.writeFunction = writeFunction;
    }

    /**
     * Initializes a new {@link PacketCodec} instance using the provided {@link SystemSocket} to read and write bytes.
     */
    public static PacketCodec from(final SystemSocket systemSocket) {
        return new PacketCodec(systemSocket::readFully, systemSocket::write);
    }

    /**
     * Attempts to read a packet using the read function provided during initialization.
     * <p>
     * This will return a null value when a packet is not available to be read yet. Consider calling this within a
     * scheduled task or a while loop.
     *
     * @return The {@link Packet} if it could be read, or null if the read function was unable to complete reading the
     * incoming data. This usually occurs when the socket is closed by the other party.
     */
    public @Nullable Packet read() throws IOException {
        // We can attempt to read the header into the header buffer. If that does not get read fully, there's nothing we
        // can do yet.
        if (!this.readFunction.readFully(this.headerBuffer)) {
            return null;
        }

        // The header contains an opcode and the length of the JSON payload within the packet.
        final int opcodeValue = this.headerBuffer.getInt();
        final int payloadLength = this.headerBuffer.getInt();

        // We're finished with the header buffer, we can reset it to its old position.
        this.headerBuffer.clear();

        if (opcodeValue < 1 || opcodeValue > PacketOpcode.values().length) {
            return null;
        }

        final PacketOpcode opcode = PacketOpcode.values()[opcodeValue];

        // We can then read the JSON within the packet, now that we know the length.
        final ByteBuffer payloadBuffer = ByteBuffer.allocate(payloadLength);
        if (!this.readFunction.readFully(payloadBuffer)) {
            return null;
        }

        final String payload = StandardCharsets.UTF_8.decode(payloadBuffer).toString();
        System.out.println("Payload: " + payload);

        switch (opcode) {
            case FRAME:
                return this.jsonMapper.readValue(payload, IncomingFramePacket.class);

            case CLOSE:
                return this.jsonMapper.readValue(payload, ClosePacket.class);

            case PING:
                return this.jsonMapper.readValue(payload, PingPacket.class);

            case PONG:
                return this.jsonMapper.readValue(payload, PongPacket.class);

            default:
                throw new IllegalStateException("Unsupported packet opcode " + opcode);
        }
    }

    /**
     * Attempts to write a packet using the write function provided during initialization.
     */
    public void write(final Packet packet) throws IOException {
        // In order to allocate a buffer, we need to know how much data to allocate.
        final byte[] payloadBytes = this.jsonMapper.writeValueAsBytes(packet);

        final ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE_BYTES + payloadBytes.length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // The header is written in little-endian.
        buffer.putInt(packet.opcode().ordinal());
        buffer.putInt(payloadBytes.length);

        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(payloadBytes);

        // Now that we have the buffer, we can write that to the socket.
        this.writeFunction.write(buffer);
    }

    /**
     * The type of the function called when bytes should be read to decode a packet.
     */
    public interface ReadFunction {
        /**
         * Attempts to read bytes from the underlying data source into the provided {@link ByteBuffer}.
         * <p></p>
         * If there is not enough bytes available to read into the buffer, this will block until the bytes become
         * available. The {@link ByteBuffer} must be array-backed for this operation to succeed.
         *
         * @param byteBuffer The byte buffer to read into.
         * @return Whether the operation was successful, if `false`, it's likely that the socket was closed.
         */
        boolean readFully(final ByteBuffer byteBuffer) throws IOException;
    }

    /**
     * The type of the function called when bytes should be written after encoding a packet.
     */
    public interface WriteFunction {
        /**
         * Attempts to write bytes to the underlying data source from the provided {@link ByteBuffer}.
         *
         * @param byteBuffer The buffer to write the bytes from.
         * @throws IOException If an I/O error occurs.
         */
        void write(final ByteBuffer byteBuffer) throws IOException;
    }
}
