package org.benetech.servicenet.matching.service;

import org.benetech.servicenet.matching.model.EntityEquivalent;
import org.benetech.servicenet.matching.model.WrappedEquivalent;

import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

class EntityEquivalentTestUtils {

    static<V extends EntityEquivalent> boolean isMatchBetweenEntities(Set<V> set, UUID baseID, UUID partnerId) {
        return set.stream().anyMatch(s ->
            s.getBaseResourceId().equals(baseID) && s.getPartnerResourceId().equals(partnerId)
        );
    }

    static boolean isMatchBetweenEntities(WrappedEquivalent equivalent, Class<?> clazz, UUID baseID, UUID partnerId) {
        return isMatchBetweenEntities(equivalent.getEntitiesByClass(clazz), baseID, partnerId);
    }

    static <E extends WrappedEquivalent, V> void verifyIfEmptyResult(EntityEquivalentsService<E, V> service,
                                                                     V objectWithNullValue, V objectWithRandomValues,
                                                                     Class<?> clazz) {
        WrappedEquivalent firstValueNullResult =
            service.generateEquivalent(objectWithNullValue, objectWithRandomValues);
        WrappedEquivalent secondValueNullResult =
            service.generateEquivalent(objectWithRandomValues, objectWithNullValue);
        WrappedEquivalent bothValuesNullResult =
            service.generateEquivalent(objectWithNullValue, objectWithNullValue);

        assertTrue(firstValueNullResult.getEntitiesByClass(clazz).isEmpty());
        assertTrue(secondValueNullResult.getEntitiesByClass(clazz).isEmpty());
        assertTrue(bothValuesNullResult.getEntitiesByClass(clazz).isEmpty());
    }
}
