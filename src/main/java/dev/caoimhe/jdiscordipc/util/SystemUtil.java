package dev.caoimhe.jdiscordipc.util;

import org.jspecify.annotations.Nullable;

import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemUtil {
    private static @Nullable Long cachedProcessId = null;

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

    /**
     * Returns the current process identifier for this running JVM.
     */
    public static long getProcessId() {
        // If we've already evaluated the process ID, we can return it, it won't change.
        if (SystemUtil.cachedProcessId != null) {
            return SystemUtil.cachedProcessId;
        }

        // getPid is only available on Java 10 and higher. We are targetting Java 8+.
        final String processName = ManagementFactory.getRuntimeMXBean().getName();
        final String processIdString = processName.split("@")[0];
        final long processId = Long.parseLong(processIdString);

        SystemUtil.cachedProcessId = processId;
        return processId;
    }
}
