package org.benetech.servicenet.service;

import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.web.rest.Suggestions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Activity.
 */
public interface ActivityService {

    Page<ActivityDTO> getAllOrganizationActivities(Pageable pageable, UUID systemAccountId,
    String search, ActivityFilterDTO activityFilterDTO);

    Optional<ActivityRecordDTO> getOneByOrganizationId(UUID orgId);

    Suggestions getNameSuggestions(ActivityFilterDTO activityFilterDTO, UUID systemAccountId, String search);
}
