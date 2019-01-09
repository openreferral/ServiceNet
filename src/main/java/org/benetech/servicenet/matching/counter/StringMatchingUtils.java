package org.benetech.servicenet.matching.counter;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public final class StringMatchingUtils {

    private static final String DELIMITER = " ";

    public static String normalize(String name) {
        return name.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9 ]", "").trim().replaceAll(" +", " ");
    }

    public static String sort(String name) {
        return Arrays.stream(name.split(DELIMITER)).sorted().collect(Collectors.joining(DELIMITER));
    }

    public static String extractInitials(String name) {
        return Arrays.stream(name.split(DELIMITER)).map(s -> s.substring(0, 1)).collect(Collectors.joining());
    }

    private StringMatchingUtils() {
    }
}
