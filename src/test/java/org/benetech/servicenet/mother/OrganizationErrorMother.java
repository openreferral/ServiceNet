package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.OrganizationError;

public final class OrganizationErrorMother {

    public static final String ORGANIZATION_ERROR = "Organization Error";

    public static OrganizationError createDefault() {
        return new OrganizationError()
            .cause(ORGANIZATION_ERROR);
    }

    private OrganizationErrorMother() {
    }
}
