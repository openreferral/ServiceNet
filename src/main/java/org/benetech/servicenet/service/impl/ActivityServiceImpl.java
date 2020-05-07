package org.benetech.servicenet.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.repository.ActivityRepository;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.ExclusionsConfigService;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.service.dto.ProviderRecordDTO;
import org.benetech.servicenet.service.exceptions.ActivityCreationException;
import org.benetech.servicenet.service.dto.Suggestions;
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

    private final OrganizationMatchService organizationMatchService;

    private final OrganizationService organizationService;

    private final UserService userService;

    public ActivityServiceImpl(ActivityRepository activityRepository, RecordsService recordsService,
        ExclusionsConfigService exclusionsConfigService, OrganizationMatchService organizationMatchService,
        OrganizationService organizationService, UserService userService) {
        this.activityRepository = activityRepository;
        this.recordsService = recordsService;
        this.exclusionsConfigService = exclusionsConfigService;
        this.organizationMatchService = organizationMatchService;
        this.organizationService = organizationService;
        this.userService = userService;
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
            Optional<ActivityRecordDTO> opt = recordsService.getRecordFromOrganization(
                organizationService.findOne(orgId).get()
            );
            ActivityRecordDTO record = opt.orElseThrow(() -> new ActivityCreationException(
                String.format("Activity record couldn't be created for organization: %s", orgId)));

            return Optional.of(record);
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityRecordDTO> getPartnerActivitiesByOrganizationId(UUID orgId) {
        return organizationMatchService.findAllNotHiddenForOrganization(orgId).stream().filter(match -> !match.isDismissed())
            .map(match -> {
                try {
                    return recordsService.getRecordFromOrganization(
                        organizationService.findOne(match.getPartnerVersionId()).get()
                    ).get();
                } catch (IllegalAccessException | NoSuchElementException e) {
                    throw new ActivityCreationException(
                        String.format("Activity record couldn't be created for organization: %s",
                            match.getPartnerVersionId()));
                }
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderRecordDTO> getPartnerActivitiesForCurrentUser() {
        UserProfile userProfile = userService.getCurrentUserProfile();
        List<Organization> organizations = organizationService.findAllByUserProfile(userProfile);
        return organizations.stream()
            .map(this::getProviderRecordDTO)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @Override
    public Suggestions getNameSuggestions(
        ActivityFilterDTO activityFilterDTO, UUID systemAccountId, String search) {

        Page<ActivityInfo> activities = (systemAccountId != null) ?
            activityRepository.findAllWithFilters(systemAccountId, search, activityFilterDTO, Pageable.unpaged())
            : Page.empty();
        List<String> orgNames = activities.stream()
            .map(ActivityInfo::getName)
            .distinct().collect(Collectors.toList());
        List<String> serviceNames = activities.stream()
            .map(ActivityInfo::getOrganization).flatMap(o -> o.getServices().stream())
            .map(org.benetech.servicenet.domain.Service::getName)
            .distinct().collect(Collectors.toList());
        return new Suggestions(orgNames, serviceNames);
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

    private ProviderRecordDTO getProviderRecordDTO(Organization organization) {
        try {
            Optional<ProviderRecordDTO> opt = recordsService.getProviderRecordFromOrganization(organization);
            return opt.orElse(null);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
