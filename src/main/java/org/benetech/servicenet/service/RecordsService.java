package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.ProviderRecordDTO;

public interface RecordsService {

    Optional<ActivityRecordDTO> getRecordFromOrganization(Organization organization) throws IllegalAccessException;

    Optional<ProviderRecordDTO> getProviderRecordFromOrganization(Organization organization) throws IllegalAccessException;

    ActivityDTO getActivityDTOFromActivityInfo(ActivityInfo activityInfo, Map<UUID, ExclusionsConfig> exclusionsMap);
}
