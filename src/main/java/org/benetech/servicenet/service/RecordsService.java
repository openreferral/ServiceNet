package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface RecordsService {

    Optional<ActivityRecordDTO> getRecordFromActivityInfo(ActivityInfo activityInfo) throws IllegalAccessException;

    ActivityDTO getActivityDTOFromActivityInfo(ActivityInfo activityInfo, Map<UUID, ExclusionsConfig> exclusionsMap);
}
