package org.benetech.servicenet.matching.service.impl;

import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.matching.model.EntityEquivalent;
import org.benetech.servicenet.matching.model.OrganizationEquivalent;
import org.benetech.servicenet.matching.service.EntityEquivalentsService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class OrganizationEquivalentsService implements EntityEquivalentsService<OrganizationEquivalent, Organization> {

    @Override
    public OrganizationEquivalent generateEquivalent(Organization base, Organization partner) {
        OrganizationEquivalent result = new OrganizationEquivalent();
        result.setClazz(Organization.class);
        result.setBaseResourceId(base.getId());
        result.setPartnerResourceId(partner.getId());
        result.setOtherEquivalents(generateOtherEquivalents(base, partner));
        return result;
    }

    private Set<EntityEquivalent> generateOtherEquivalents(Organization base, Organization partner) {
        Set<EntityEquivalent> result = new HashSet<>();
        if (base.getLocation() != null && partner.getLocation() != null) {
            result.add(new EntityEquivalent(base.getLocation().getId(), partner.getLocation().getId(), Location.class));
        }
        if (base.getFunding() != null && partner.getFunding() != null) {
            result.add(new EntityEquivalent(base.getFunding().getId(), partner.getFunding().getId(), Funding.class));
        }
        return result;
    }
}
