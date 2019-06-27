package org.benetech.servicenet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.domain.view.ActivityRecord;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.service.factory.records.RecordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class RecordsServiceImpl implements RecordsService {

    @Autowired
    private RecordFactory recordFactory;

    @Override
    public Optional<ActivityRecordDTO> getRecordFromActivityInfo(ActivityRecord activityRecord) {
        return recordFactory.getFilteredRecord(activityRecord);
    }

    @Override
    public ActivityDTO getActivityDTOFromActivityInfo(ActivityInfo activityInfo,
        Map<UUID, ExclusionsConfig> exclusionsMap) {
        return recordFactory.getFilteredResult(activityInfo, exclusionsMap);
    }
}
