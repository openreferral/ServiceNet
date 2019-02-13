package org.benetech.servicenet.service.factory.records.builder;

import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.FieldExclusion;

import java.util.Set;

final class FilteredEntityBuilder {

    static <E extends AbstractEntity> E buildObject(E entity, Class<E> clazz, Set<FieldExclusion> exclusions)
        throws IllegalAccessException {
        Set<String> excludedNames = BuilderUtils.getFieldNamesFromExclusions(exclusions, clazz);
        BuilderUtils.resetExcludedFields(entity, excludedNames, clazz);
        return entity;
    }

    static <E extends AbstractEntity> Set<E> buildCollection(Set<E> entities, Class<E> clazz, Set<FieldExclusion> exclusions)
        throws IllegalAccessException {
        Set<String> excludedNames = BuilderUtils.getFieldNamesFromExclusions(exclusions, clazz);
        for (E entity : entities) {
            BuilderUtils.resetExcludedFields(entity, excludedNames, clazz);
        }

        return entities;
    }

    private FilteredEntityBuilder() {
    }
}
