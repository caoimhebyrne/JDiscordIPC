package dev.caoimhe.jdiscordipc.core.packet;

/** @see <a href="https://github.com/discord/discord-rpc/blob/master/src/rpc_connection.h">rpc_connection.h</a> */
public enum PacketOpcode {
    HANDSHAKE,
    FRAME,
    CLOSE,
    PING,
    PONG
}
