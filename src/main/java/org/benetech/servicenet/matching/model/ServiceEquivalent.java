package org.benetech.servicenet.matching.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceEquivalent extends EntityEquivalent implements WrappedEquivalent {

    private ServiceAtLocationEquivalent serviceAtLocationEquivalent;

    private RegularScheduleEquivalent regularScheduleEquivalent;

    @Override
    public Set<EntityEquivalent> getUnwrappedEntities() {
        Set<EntityEquivalent> result = new HashSet<>(otherEquivalents);
        result.addAll(regularScheduleEquivalent.getUnwrappedEntities());
        result.addAll(serviceAtLocationEquivalent.getUnwrappedEntities());
        return result;
    }
}
