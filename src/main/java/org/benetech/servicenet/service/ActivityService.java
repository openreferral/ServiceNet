package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.service.dto.ProviderRecordDTO;
import org.benetech.servicenet.service.dto.Suggestions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Activity.
 */
public interface ActivityService {

    Page<ActivityDTO> getAllOrganizationActivities(Pageable pageable, UUID systemAccountId,
    String search, ActivityFilterDTO activityFilterDTO);

    Optional<ActivityRecordDTO> getOneByOrganizationId(UUID orgId);

    List<ActivityRecordDTO> getPartnerActivitiesByOrganizationId(UUID orgId);

    List<ProviderRecordDTO> getPartnerActivitiesForCurrentUser();

    Suggestions getNameSuggestions(ActivityFilterDTO activityFilterDTO, UUID systemAccountId, String search);
}
