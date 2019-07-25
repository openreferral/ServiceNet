package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.service.dto.FiltersActivityDTO;
import org.benetech.servicenet.web.rest.SearchOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Activity.
 */
public interface ActivityService {

    Page<ActivityDTO> getAllOrganizationActivities(Pageable pageable, UUID systemAccountId,
    String search, SearchOn searchOn, FiltersActivityDTO filtersForActivity);

    Optional<ActivityRecordDTO> getOneByOrganizationId(UUID orgId);
}
