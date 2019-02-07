package org.benetech.servicenet.service.factory.records.builder;

import org.benetech.servicenet.domain.FieldExclusion;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

final class BuilderUtils {

    static Set<String> getFieldNamesFromExclusions(Set<FieldExclusion> exclusions, Class clazz) {
        return exclusions.stream()
            .filter(e -> e.getEntity().equals(clazz.getCanonicalName()))
            .flatMap(e -> e.getExcludedFields().stream())
            .collect(Collectors.toSet());
    }

    static void resetExcludedFields(Object object, Set<String> excludedNames, Class clazz) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {
            if (excludedNames.contains(field.getName())) {
                resetField(object, field);
            }
        }
    }

    private static void resetField(Object object, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(object, null);
    }

    private BuilderUtils() {
    }
}
