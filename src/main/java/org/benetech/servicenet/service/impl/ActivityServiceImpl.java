package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.comparator.ConflictsComparator;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
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
import java.util.Collections;
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

    public ActivityServiceImpl(OrganizationService organizationService, ConflictService conflictService,
                               OrganizationMatchService organizationMatchService) {
        this.organizationService = organizationService;
        this.conflictService = conflictService;
        this.organizationMatchService = organizationMatchService;
    }

    @Override
    public List<ActivityDTO> getAllOrganizationActivities(UUID systemAccountId) {
        List<ActivityDTO> activities = new ArrayList<>();
        List<OrganizationDTO> orgs = organizationService.findAllWithOwnerId(systemAccountId);

        for (OrganizationDTO org : orgs) {
            try {
                Optional<ActivityDTO> activityOpt = getEntityActivity(org.getId(), org.getId());
                activityOpt.ifPresent(activities::add);
            } catch (ActivityCreationException ex) {
                log.error(ex.getMessage());
            }
        }

        return activities;
    }

    @Override
    public Optional<ActivityDTO> getOneByOrganizationId(UUID organizationId) {
        return getEntityActivity(organizationId, organizationId);
    }

    @Override
    public Page<ActivityDTO> getAllOrganizationActivities(Pageable pageable, UUID systemAccountId) {
        List<ActivityDTO> activities = getAllOrganizationActivities(systemAccountId);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ActivityDTO> list;

        if (activities.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, activities.size());
            list = activities.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), activities.size());
    }

    private Optional<ActivityDTO> getEntityActivity(UUID orgId, UUID resourceId) throws ActivityCreationException {
        log.debug("Creating Activity for organization: {} with resourceId: {}", orgId, resourceId);
        Optional<OrganizationDTO> opt = organizationService.findOne(orgId);
        OrganizationDTO org = opt.orElseThrow(() -> new ActivityCreationException(
            String.format("There is no organization for orgId: %s", orgId)));

        List<ConflictDTO> conflictDTOS = conflictService.findAllWithResourceId(resourceId);
        conflictDTOS.sort(new ConflictsComparator());

        if (CollectionUtils.isEmpty(conflictDTOS)) {
            return Optional.empty();
        } else {
            Optional<ZonedDateTime> lastUpdated = conflictService.findMostRecentOfferedValueDate(resourceId);
            List<OrganizationMatchDTO> matches = organizationMatchService.findAllForOrganization(orgId);

            return Optional.of(ActivityDTO.builder()
                .conflicts(conflictDTOS)
                .organization(org)
                .organizationMatches(matches)
                .lastUpdated(lastUpdated.orElse(ZonedDateTime.now()))
                .build());
        }
    }
}
