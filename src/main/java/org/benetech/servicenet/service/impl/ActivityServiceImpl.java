package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.benetech.servicenet.service.dto.RecordDTO;
import org.benetech.servicenet.service.exceptions.ActivityCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing Activity.
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private final OrganizationService organizationService;

    private final OrganizationMatchService organizationMatchService;

    private final ConflictService conflictService;

    private final RecordsService recordsService;

    public ActivityServiceImpl(OrganizationService organizationService, ConflictService conflictService,
                               OrganizationMatchService organizationMatchService, RecordsService recordsService) {
        this.organizationService = organizationService;
        this.conflictService = conflictService;
        this.organizationMatchService = organizationMatchService;
        this.recordsService = recordsService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityDTO> getAllOrganizationActivities(Pageable pageable, UUID systemAccountId) {
        List<ActivityDTO> activities = new ArrayList<>();
        Pageable pageOnly = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Organization> orgs = organizationService.findAllWithOwnerId(systemAccountId, pageOnly);

        for (Organization org : orgs) {
            try {
                Optional<ActivityDTO> activityOpt = getEntityActivity(org.getId());
                activityOpt.ifPresent(activities::add);
            } catch (ActivityCreationException ex) {
                log.error(ex.getMessage());
            }
        }

        return new PageImpl<>(
            activities, PageRequest.of(pageOnly.getPageNumber(), pageOnly.getPageSize()), orgs.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ActivityDTO> getOneByOrganizationId(UUID organizationId) {
        return getEntityActivity(organizationId);
    }

    private Optional<ActivityDTO> getEntityActivity(UUID orgId) throws ActivityCreationException {
        log.debug("Creating Activity for organization: {}", orgId);
        try {
            Optional<RecordDTO> opt = recordsService.getRecordFromOrganization(orgId, orgId);
            RecordDTO record = opt.orElseThrow(() -> new ActivityCreationException(
                String.format("Activity record couldn't be created for organization: %s", orgId)));

            if (CollectionUtils.isEmpty(record.getConflicts())) {
                return Optional.empty();
            } else {
                Optional<ZonedDateTime> lastUpdated = conflictService.findMostRecentOfferedValueDate(orgId);
                List<OrganizationMatchDTO> matches = organizationMatchService.findAllForOrganization(orgId);

                return Optional.of(ActivityDTO.builder()
                    .record(record)
                    .organizationMatches(matches)
                    .lastUpdated(lastUpdated.orElse(ZonedDateTime.now()))
                    .build());
            }
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
    }
}
