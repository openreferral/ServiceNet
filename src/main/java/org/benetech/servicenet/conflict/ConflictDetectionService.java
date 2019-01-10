package org.benetech.servicenet.conflict;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.OrganizationMatch;

import java.util.List;

public interface ConflictDetectionService {

    /**
     * Detect and persist conflicts for entities asynchronously.
     *
     * @param matches a list of organization matches
     */
    List<Conflict> detect(List<OrganizationMatch> matches);
}
