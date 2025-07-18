package dev.caoimhe.jdiscordipc.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemUtil {
    private SystemUtil() {}

    /**
     * Returns the temporary directory used for Discord's unix domain socket.
     */
    public static Path getTemporaryDirectory() {
        final String xdgRuntimeDirectory = System.getenv("XDG_RUNTIME_DIR");
        if (xdgRuntimeDirectory != null) {
            return Paths.get(xdgRuntimeDirectory);
        }

        final String temporaryDirectory = System.getenv("TMPDIR");
        if (temporaryDirectory != null) {
            return Paths.get(temporaryDirectory);
        }

        final String temporary = System.getenv("TMP");
        if (temporary != null) {
            return Paths.get(temporary);
        }

        throw new IllegalStateException("No XDG_RUNTIME_DIR, TMPDIR or TMP environment variable set!");
    }
}
