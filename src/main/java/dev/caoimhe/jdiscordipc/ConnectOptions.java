package dev.caoimhe.jdiscordipc;

import org.jspecify.annotations.Nullable;

import java.nio.file.Path;

/**
 * Extra options that can be used to control the behavior of {@link JDiscordIPC#connect()}.
 *
 * @see ConnectOptions.Builder
 */
public class ConnectOptions {
    private final @Nullable Integer socketIndex;
    private final @Nullable Path temporaryDirectory;

    public ConnectOptions(final @Nullable Integer socketIndex, final @Nullable Path temporaryDirectory) {
        this.socketIndex = socketIndex;
        this.temporaryDirectory = temporaryDirectory;
    }

    /**
     * The index of the Discord IPC socket (e.g. {@code /tmp/discord-ipc-2}). Must be an integer between {@code 0} and
     * {@code 9}.
     */
    public @Nullable Integer socketIndex() {
        return this.socketIndex;
    }

    /**
     * The temporary directory to use when locating the Discord IPC socket, instead of attempting to read from various
     * environment variables.
     */
    public @Nullable Path temporaryDirectory() {
        return this.temporaryDirectory;
    }

    /**
     * Returns a {@link Builder} to construct an instance of {@link ConnectOptions}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@link ConnectOptions}.
     */
    public static class Builder {
        private @Nullable Integer socketIndex = null;
        private @Nullable Path temporaryDirectory = null;

        /**
         * @see ConnectOptions#builder().
         */
        private Builder() {
        }

        /**
         * Sets the index of the Discord IPC socket.
         *
         * @see ConnectOptions#socketIndex()
         */
        public Builder socketIndex(final @Nullable Integer socketIndex) {
            this.socketIndex = socketIndex;
            return this;
        }

        /**
         * Sets the temporary directory to use when locating the Discord IPC socket.
         *
         * @see ConnectOptions#temporaryDirectory()
         */
        public Builder temporaryDirectory(final @Nullable Path temporaryDirectory) {
            this.temporaryDirectory = temporaryDirectory;
            return this;
        }

        /**
         * Builds an instance of {@link ConnectOptions} from the properties set on this {@link Builder}.
         */
        public ConnectOptions build() {
            return new ConnectOptions(
                /* socketIndex */ this.socketIndex,
                /* temporaryDirectory */ this.temporaryDirectory
            );
        }
    }
}
