package dev.caoimhe.jdiscordipc.internal.util;

import org.jspecify.annotations.Nullable;

import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class SystemUtil {
    private static final String OS_NAME = System.getProperty("os.name");
    private static @Nullable Long cachedProcessId = null;

    private SystemUtil() {
    }

    /**
     * Returns whether the current OS is Windows.
     */
    public static boolean isWindows() {
        return OS_NAME.toLowerCase(Locale.ROOT).startsWith("windows");
    }

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

        return Paths.get("/tmp");
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
