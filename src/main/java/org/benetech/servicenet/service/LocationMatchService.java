package org.benetech.servicenet.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.benetech.servicenet.domain.LocationMatch;
import org.benetech.servicenet.service.dto.LocationMatchDto;

/**
 * Service Interface for managing LocationMatch.
 */
public interface LocationMatchService {

    LocationMatchDto save(LocationMatchDto locationMatchDto);

    List<LocationMatchDto> findAllDtoForLocation(UUID locationId);

    LocationMatch saveOrUpdate(LocationMatch locationMatch);

    Set<LocationMatch> findAllForLocation(UUID locationId);

    void delete(UUID locationId, UUID matchingLocationId);
}
