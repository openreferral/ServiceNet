package org.benetech.servicenet.matching;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public final class StringMatchingUtils {

    public static String normalize(String name) {
        return name.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9 ]", "").trim().replaceAll(" +", " ");
    }

    public static String sort(String name) {
        String delimiter = " ";
        return Arrays.stream(name.split(delimiter)).sorted().collect(Collectors.joining(delimiter));
    }

    public static String extractInitials(String name) {
        String delimiter = " ";
        return Arrays.stream(name.split(delimiter)).map(s -> s.substring(0, 1)).collect(Collectors.joining());
    }

    private StringMatchingUtils() {
    }
}
