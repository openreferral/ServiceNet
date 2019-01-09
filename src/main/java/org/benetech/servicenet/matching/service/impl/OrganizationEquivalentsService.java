package org.benetech.servicenet.matching.service.impl;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.matching.model.OrganizationEquivalent;
import org.benetech.servicenet.matching.service.EntityEquivalentsService;
import org.springframework.stereotype.Component;

@Component
public class OrganizationEquivalentsService implements EntityEquivalentsService<OrganizationEquivalent, Organization> {

    @Override
    public OrganizationEquivalent generateEquivalent(Organization base, Organization partner) {
        return null;
    }
}
