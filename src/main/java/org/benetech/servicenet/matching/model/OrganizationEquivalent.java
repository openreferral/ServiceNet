package org.benetech.servicenet.matching.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationEquivalent extends EntityEquivalent implements WrappedEquivalent {

    private Set<ServiceEquivalent> serviceEquivalents = new HashSet<>();

    @Override
    public Set<EntityEquivalent> getUnwrappedEntities() {
        Set<EntityEquivalent> result = new HashSet<>(otherEquivalents);
        for (ServiceEquivalent serviceEquivalent : serviceEquivalents) {
            result.addAll(serviceEquivalent.getUnwrappedEntities());
        }
        return result;
    }
}
