package org.benetech.servicenet.service.factory.records.builder;

import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.domain.Organization;

import java.util.Set;

final class OrganizationBuilder {

    static Organization buildFilteredOrganization(Organization organization, Set<FieldExclusion> exclusions)
        throws IllegalAccessException {
        Set<String> excludedNames = BuilderUtils.getFieldNamesFromExclusions(exclusions, Organization.class);
        BuilderUtils.resetExcludedFields(organization, excludedNames, Organization.class);
        return organization;
    }

    private OrganizationBuilder() {
    }
}
