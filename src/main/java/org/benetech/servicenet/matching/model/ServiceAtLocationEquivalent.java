package org.benetech.servicenet.matching.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceAtLocationEquivalent extends EntityEquivalent implements WrappedEquivalent {

    private RegularScheduleEquivalent regularScheduleEquivalent;

    @Override
    public Set<EntityEquivalent> getUnwrappedEntities() {
        Set<EntityEquivalent> result = new HashSet<>(otherEquivalents);
        result.addAll(regularScheduleEquivalent.getUnwrappedEntities());
        return result;
    }
}
