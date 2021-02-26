package org.benetech.servicenet.service.impl;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import com.google.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.UserGroup;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.domain.view.ActivityInfo;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.repository.ActivityRepository;
import org.benetech.servicenet.repository.ProviderRecordsRepository;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.ExclusionsConfigService;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.service.dto.OrganizationWithLocationsOptionDTO;
import org.benetech.servicenet.service.dto.provider.ProviderRecordDTO;
import org.benetech.servicenet.service.dto.provider.ProviderRecordForMapDTO;
import org.benetech.servicenet.service.dto.Suggestions;
import org.benetech.servicenet.service.dto.provider.DeactivatedOrganizationDTO;
import org.benetech.servicenet.service.dto.provider.ProviderFilterDTO;
import org.benetech.servicenet.service.exceptions.ActivityCreationException;
import org.benetech.servicenet.service.mapper.OrganizationMapper;
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

    private final ProviderRecordsRepository providerRecordsRepository;

    private final OrganizationMapper organizationMapper;

    private final UserProfileRepository userProfileRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository, RecordsService recordsService,
        ExclusionsConfigService exclusionsConfigService, OrganizationMatchService organizationMatchService,
        OrganizationService organizationService, UserService userService,
        ProviderRecordsRepository providerRecordsRepository, OrganizationMapper organizationMapper,
        UserProfileRepository userProfileRepository) {
        this.activityRepository = activityRepository;
        this.recordsService = recordsService;
        this.exclusionsConfigService = exclusionsConfigService;
        this.organizationMatchService = organizationMatchService;
        this.organizationService = organizationService;
        this.userService = userService;
        this.providerRecordsRepository = providerRecordsRepository;
        this.organizationMapper = organizationMapper;
        this.userProfileRepository = userProfileRepository;
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
                            match.getPartnerVersionId()), e);
                }
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderRecordDTO> getPartnerActivitiesForCurrentUser(Pageable pageable) {
        UserProfile userProfile = userService.getCurrentUserProfile();
        Set<UserGroup> userGroups = userProfile.getUserGroups();
        List<UserProfile> userProfiles;
        if (userGroups == null || userGroups.isEmpty()) {
            userProfiles = Collections.singletonList(userProfile);
        } else {
            userProfiles = userProfileRepository.findAllWithUserGroups(new ArrayList<>(userGroups));
        }
        Page<ProviderRecordDTO> providerRecords = providerRecordsRepository
            .findAllWithFilters(userProfiles,
                null, new ProviderFilterDTO(), null, pageable);
        return filterProviderRecords(providerRecords);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationWithLocationsOptionDTO> getOrganizationOptionsForCurrentUser() {
        UserProfile userProfile = userService.getCurrentUserProfile();
        Set<UserGroup> userGroups = userProfile.getUserGroups();
        List<UserProfile> userProfiles;
        if (userGroups == null || userGroups.isEmpty()) {
            userProfiles = Collections.singletonList(userProfile);
        } else {
            userProfiles = userProfileRepository.findAllWithUserGroups(new ArrayList<>(userGroups));
        }
        return providerRecordsRepository
            .findAllOptions(userProfiles);
    };

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderRecordDTO> getAllPartnerActivities(ProviderFilterDTO providerFilterDTO,
        String search, Pageable pageable) {
        UserProfile currentUserProfile = userService.getCurrentUserProfile();
        Page<ProviderRecordDTO> providerRecords = providerRecordsRepository
            .findAllWithFilters(null, currentUserProfile, providerFilterDTO, search, pageable);
        return filterProviderRecords(providerRecords);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderRecordDTO> getAllPartnerActivitiesPublic(ProviderFilterDTO providerFilterDTO,
        Silo silo, String search, Pageable pageable) {
        Page<ProviderRecordDTO> providerRecords = providerRecordsRepository
            .findAllWithFiltersPublic(providerFilterDTO, silo, search, pageable);
        return filterProviderRecords(providerRecords);
    }

    @Override
    public Page<ProviderRecordDTO> getAllPartnerActivities(UUID siloId,
        UUID systemAccountId,
        Pageable pageable) {
        Page<ProviderRecordDTO> providerRecords = providerRecordsRepository
            .findAllBySiloAndSystemAccount(siloId, systemAccountId, pageable);
        return filterProviderRecords(providerRecords, systemAccountId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderRecordDTO> getRecordsToClaim(Pageable pageable, String search) {
        return providerRecordsRepository.findRecordsToClaim(pageable, search);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderRecordForMapDTO> getAllPartnerActivitiesForMap(
        Pageable pageable, ProviderFilterDTO providerFilterDTO,
        String search, List<Double> boundaries, LatLng center) {
        UserProfile userProfile = userService.getCurrentUserProfile();
        List<ExclusionsConfig> exclusions = exclusionsConfigService.findAllBySystemAccountName(SERVICE_PROVIDER);
        return providerRecordsRepository
            .findProviderRecordsForMap(pageable, userProfile, providerFilterDTO, search, exclusions, boundaries, center);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderRecordForMapDTO> getAllPartnerActivitiesForMap(
        Pageable pageable, ProviderFilterDTO providerFilterDTO,
        String search, Silo silo, List<Double> boundaries, LatLng center) {
        List<ExclusionsConfig> exclusions = exclusionsConfigService.findAllBySystemAccountName(SERVICE_PROVIDER);
        return providerRecordsRepository
            .findAllWithFiltersForMap(pageable, silo, providerFilterDTO, search, exclusions, boundaries, center);
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderRecordDTO getPartnerActivityById(UUID id) {
        Optional<Organization> optOrganization = organizationService.findOne(id);
        return optOrganization.map(this::getProviderRecordDTO).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderRecordDTO getPartnerActivityById(UUID id, Silo silo) {
        Optional<Organization> optOrganization = organizationService.findOneByIdAndSilo(id, silo);
        if (optOrganization.isEmpty()) {
            throw new BadRequestAlertException("There is no organization with this id and silo", "providerRecord", "idnull");
        }
        return optOrganization.map(this::getProviderRecordDTO).orElse(null);
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

    @Override
    public List<DeactivatedOrganizationDTO> getAllDeactivatedRecords() {
        List<Organization> organizations = organizationService
            .findAllByAccountNameAndNotActiveAndCurrentUser();
        return organizations.stream()
            .map(this.organizationMapper::toDeactivatedOrganizationDto)
            .collect(Collectors.toList());
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

    private Page<ProviderRecordDTO> filterProviderRecords(Page<ProviderRecordDTO> providerRecords) {
        return filterProviderRecords(providerRecords, null);
    }

    private Page<ProviderRecordDTO> filterProviderRecords(Page<ProviderRecordDTO> providerRecords, UUID systemAccountId) {
        return recordsService.filterProviderRecords(providerRecords, systemAccountId);
    }
}
