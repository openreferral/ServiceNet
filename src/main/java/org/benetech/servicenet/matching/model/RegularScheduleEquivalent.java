package org.benetech.servicenet.matching.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegularScheduleEquivalent extends EntityEquivalent implements WrappedEquivalent {

    @Override
    public Set<EntityEquivalent> getUnwrappedEntities() {
        return otherEquivalents;
    }
}
