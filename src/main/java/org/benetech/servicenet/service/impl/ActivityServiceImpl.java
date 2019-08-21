package org.benetech.servicenet.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.domain.view.ActivityRecord;
import org.benetech.servicenet.repository.ActivityRepository;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.ExclusionsConfigService;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.service.exceptions.ActivityCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Activity.
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private final ActivityRepository activityRepository;

    private final RecordsService recordsService;

    private final ExclusionsConfigService exclusionsConfigService;

    public ActivityServiceImpl(ActivityRepository activityRepository, RecordsService recordsService,
        ExclusionsConfigService exclusionsConfigService) {

        this.activityRepository = activityRepository;
        this.recordsService = recordsService;
        this.exclusionsConfigService = exclusionsConfigService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityDTO> getAllOrganizationActivities(Pageable pageable, UUID systemAccountId,
        String search, ActivityFilterDTO activityFilterDTO) {

        Map<UUID, ExclusionsConfig> exclusionsMap = exclusionsConfigService.getAllBySystemAccountId();

        List<ActivityDTO> activities = new ArrayList<>();
        Page<ActivityInfo> activitiesInfo = findAllActivitiesInfoWithOwnerId(systemAccountId, search, pageable,
            activityFilterDTO);
        long totalElements = activitiesInfo.getTotalElements();
        for (ActivityInfo info : activitiesInfo) {
            try {
                activities.add(getEntityActivity(info, exclusionsMap));
            } catch (ActivityCreationException ex) {
                log.error(ex.getMessage());
            }
        }

        return new PageImpl<>(
            activities,
            PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
            totalElements
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ActivityRecordDTO> getOneByOrganizationId(UUID orgId) {
        log.debug("Creating Activity Record for organization: {}", orgId);
        try {
            ActivityRecord activityInfo = activityRepository.findOneByOrganizationId(orgId);
            Optional<ActivityRecordDTO> opt = recordsService.getRecordFromActivityInfo(activityInfo);
            ActivityRecordDTO record = opt.orElseThrow(() -> new ActivityCreationException(
                String.format("Activity record couldn't be created for organization: %s", orgId)));

            return Optional.of(record);
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
    }

    private ActivityDTO getEntityActivity(ActivityInfo info, Map<UUID, ExclusionsConfig> exclusionsMap) {
        log.debug("Creating Activity for organization: {}", info.getId());

        return recordsService.getActivityDTOFromActivityInfo(info, exclusionsMap);
    }

    private Page<ActivityInfo> findAllActivitiesInfoWithOwnerId(UUID ownerId, String search,
        Pageable pageable, ActivityFilterDTO activityFilterDTO) {
        if (ownerId != null) {
            return activityRepository.findAllWithFilters(ownerId, search, activityFilterDTO, pageable);
        } else {
            return new PageImpl<>(Collections.emptyList(), pageable, Collections.emptyList().size());
        }
    }
}
