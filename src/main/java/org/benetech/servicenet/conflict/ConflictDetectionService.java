package org.benetech.servicenet.conflict;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;

import java.util.List;

public interface ConflictDetectionService {

    /**
     * Detect and persist conflicts for entities asynchronously.
     *
     * @param organization the organization
     * @param matches a list of organization matches
     */
    void detect(Organization organization, List<OrganizationMatch> matches);

    /**
     * Remove conflicts for organization match.
     *
     * @param match organization match
     */
    void remove(OrganizationMatch match);
}
