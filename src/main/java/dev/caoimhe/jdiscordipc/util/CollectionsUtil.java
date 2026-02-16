package dev.caoimhe.jdiscordipc.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is internal to JDiscordIPC. You should not use this in your own application!
 */
public class CollectionsUtil {
    /**
     * Returns an unmodifiable set containing the provided elements. This is supposed to be a Java 8 compatible method
     * as a replacement for {@code Set#of}.
     */
    @SafeVarargs
    public static <T> Set<T> setOf(final T... elements) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(elements)));
    }
}
