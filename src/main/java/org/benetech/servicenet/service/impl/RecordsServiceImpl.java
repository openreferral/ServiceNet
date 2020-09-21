package org.benetech.servicenet.service.impl;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.service.dto.OwnerDTO;
import org.benetech.servicenet.service.dto.ProviderRecordDTO;
import org.benetech.servicenet.service.dto.external.RecordDetailsDTO;
import org.benetech.servicenet.service.factory.records.RecordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecordsServiceImpl implements RecordsService {

    @Autowired
    private RecordFactory recordFactory;

    @Autowired
    private UserService userService;

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
        ActivityDTO activityDTO = recordFactory.getFilteredResult(activityInfo, exclusionsMap);
        if (activityInfo.getOrganization() != null) {
            OwnerDTO owner = userService.getUserDtoOfOrganization(activityInfo.getOrganization());
            activityDTO.setOwner(owner);
        }
        return activityDTO;
    }

    @Override
    public RecordDetailsDTO getRecordDetailsFromOrganization(Organization organization) {
        return recordFactory.getRecordDetails(organization);
    }

    @Override
    public Page<ProviderRecordDTO> filterProviderRecords(Page<ProviderRecordDTO> providerRecords) {
        return recordFactory.filterProviderRecords(providerRecords);
    }
}
