package org.benetech.servicenet.adapter.shared;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MapperUtils {

    public static String joinNotBlank(String delimiter, String value1, String value2) {
        return Stream.of(value1, value2).filter(StringUtils::isNotBlank).collect(Collectors.joining(delimiter));
    }

    private MapperUtils() {
    }
}
