package org.benetech.servicenet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.service.factory.records.RecordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class RecordsServiceImpl implements RecordsService {

    @Autowired
    private RecordFactory recordFactory;

    @Override
    public Optional<ActivityRecordDTO> getRecordFromActivityInfo(ActivityInfo activityInfo) {
        return recordFactory.getFilteredRecord(activityInfo);
    }

    @Override
    public ActivityDTO getActivityDTOFromActivityInfo(ActivityInfo activityInfo,
        Map<UUID, Set<FieldExclusion>> exclusionsMap) {
        return recordFactory.getFilteredResult(activityInfo, exclusionsMap);
    }
}
