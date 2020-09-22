package org.benetech.servicenet.service.factory.records.builder;

import java.util.Set;
import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.FieldExclusion;

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

    static <E extends AbstractEntity, D> D buildObject(D entity, Class<D> dto, Class<E> clazz, Set<FieldExclusion> exclusions)
        throws IllegalAccessException {
        Set<String> excludedNames = BuilderUtils.getFieldNamesFromExclusions(exclusions, clazz);
        BuilderUtils.resetExcludedFields(entity, excludedNames, dto);
        return entity;
    }

    static <E extends AbstractEntity, D> Set<D> buildCollection(Set<D> entities, Class<D> dto, Class<E> clazz, Set<FieldExclusion> exclusions)
        throws IllegalAccessException {
        Set<String> excludedNames = BuilderUtils.getFieldNamesFromExclusions(exclusions, clazz);
        for (D entity : entities) {
            BuilderUtils.resetExcludedFields(entity, excludedNames, dto);
        }

        return entities;
    }

    private FilteredEntityBuilder() {
    }
}
