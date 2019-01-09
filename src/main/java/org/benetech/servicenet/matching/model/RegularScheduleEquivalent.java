package org.benetech.servicenet.matching.model;

import lombok.Data;

import java.util.Set;

@Data
public class RegularScheduleEquivalent extends EntityEquivalent implements WrappedEquivalent {

    @Override
    public Set<EntityEquivalent> getUnwrappedEntities() {
        return otherEquivalents;
    }
}
