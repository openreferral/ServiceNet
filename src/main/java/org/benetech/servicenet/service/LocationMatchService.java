package org.benetech.servicenet.service;

import java.util.Set;
import java.util.UUID;
import org.benetech.servicenet.domain.LocationMatch;

/**
 * Service Interface for managing LocationMatch.
 */
public interface LocationMatchService {

    LocationMatch saveOrUpdate(LocationMatch locationMatch);

    Set<LocationMatch> findAllForLocation(UUID locationId);

    void delete(UUID locationId, UUID matchingLocationId);
}
