package org.benetech.servicenet.matching.model;

import java.util.Set;
import java.util.stream.Collectors;

public interface WrappedEquivalent {

    Set<EntityEquivalent> getUnwrappedEntities();

    default Set<EntityEquivalent> getEntitiesByClass(Class<?> clazz) {
        return getUnwrappedEntities().stream().filter(e -> e.getClazz().equals(clazz)).collect(Collectors.toSet());
    }
}
