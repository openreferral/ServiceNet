package org.benetech.servicenet.adapter.shared;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MapperUtils {

    public static String joinNotBlank(String delimiter, String value1, String value2) {
        return Stream.of(value1, value2).filter(StringUtils::isNotBlank).collect(Collectors.joining(delimiter));
    }

    public static Double stringToDouble(String source) {
        if (StringUtils.isNotBlank(source)) {
            return Double.valueOf(source);
        } else {
            return null;
        }
    }

    public static Integer stringToInteger(String source) {
        String numbersOnly = source.replaceAll("\\D+", "");
        if (StringUtils.isNotBlank(numbersOnly)) {
            return Integer.valueOf(numbersOnly);
        } else {
            return null;
        }
    }

    private MapperUtils() {
    }
}
