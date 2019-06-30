package org.benetech.servicenet.service.factory.records.builder;

import org.benetech.servicenet.domain.FieldExclusion;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BuilderUtils {

    private static final Logger LOG = LoggerFactory.getLogger(BuilderUtils.class);

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

    public static void setField(Object object, String fieldName, String value, Class clazz) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    private static void resetField(Object object, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(object, null);
    }

    private BuilderUtils() {
    }
}
