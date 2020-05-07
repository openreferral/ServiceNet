package org.benetech.servicenet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.service.dto.ProviderRecordDTO;
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
    public Optional<ActivityRecordDTO> getRecordFromOrganization(Organization organization) {
        return recordFactory.getFilteredRecord(organization);
    }

    @Override
    public Optional<ProviderRecordDTO> getProviderRecordFromOrganization(
        Organization organization) throws IllegalAccessException {
        return recordFactory.getFilteredProviderRecord(organization);
    }

    @Override
    public ActivityDTO getActivityDTOFromActivityInfo(ActivityInfo activityInfo,
        Map<UUID, ExclusionsConfig> exclusionsMap) {
        return recordFactory.getFilteredResult(activityInfo, exclusionsMap);
    }
}
