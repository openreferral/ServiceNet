package org.benetech.servicenet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.conflict.ConflictDetectionService;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.matching.counter.OrganizationSimilarityCounter;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.benetech.servicenet.repository.OrganizationMatchRepository;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.benetech.servicenet.service.mapper.OrganizationMatchMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing OrganizationMatch.
 */
@Slf4j
@Service
@Transactional
public class OrganizationMatchServiceImpl implements OrganizationMatchService {

    private final OrganizationMatchRepository organizationMatchRepository;

    private final OrganizationMatchMapper organizationMatchMapper;

    private final OrganizationService organizationService;

    private final OrganizationSimilarityCounter organizationSimilarityCounter;

    private final ConflictDetectionService conflictDetectionService;

    private final float orgMatchThreshold;

    public OrganizationMatchServiceImpl(OrganizationMatchRepository organizationMatchRepository,
                                        OrganizationMatchMapper organizationMatchMapper,
                                        OrganizationService organizationService,
                                        OrganizationSimilarityCounter organizationSimilarityCounter,
                                        ConflictDetectionService conflictDetectionService,
                                        @Value("${similarity-ratio.config.organization-match-threshold}")
                                            float orgMatchThreshold) {
        this.organizationMatchRepository = organizationMatchRepository;
        this.organizationMatchMapper = organizationMatchMapper;
        this.organizationService = organizationService;
        this.organizationSimilarityCounter = organizationSimilarityCounter;
        this.conflictDetectionService = conflictDetectionService;
        this.orgMatchThreshold = orgMatchThreshold;
    }

    /**
     * Save a organizationMatch.
     *
     * @param organizationMatchDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrganizationMatchDTO save(OrganizationMatchDTO organizationMatchDTO) {
        log.debug("Request to save OrganizationMatch : {}", organizationMatchDTO);

        OrganizationMatch organizationMatch = organizationMatchMapper.toEntity(organizationMatchDTO);
        organizationMatch = save(organizationMatch);
        return organizationMatchMapper.toDto(organizationMatch);
    }

    @Override
    public OrganizationMatch save(OrganizationMatch organizationMatch) {
        return organizationMatchRepository.save(organizationMatch);
    }

    /**
     * Get all the organizationMatches.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrganizationMatchDTO> findAll() {
        log.debug("Request to get all OrganizationMatches");
        return organizationMatchRepository.findAll().stream()
            .map(organizationMatchMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<OrganizationMatchDTO> findAllForOrganization(UUID orgId) {
        return organizationMatchRepository.findAllByOrganizationRecordId(orgId).stream()
            .map(organizationMatchMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one organizationMatch by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationMatchDTO> findOne(UUID id) {
        log.debug("Request to get OrganizationMatch : {}", id);
        return organizationMatchRepository.findById(id)
            .map(organizationMatchMapper::toDto);
    }

    /**
     * Delete the organizationMatch by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete OrganizationMatch : {}", id);
        organizationMatchRepository.deleteById(id);
    }

    @Async
    @Override
    public void createOrUpdateOrganizationMatches(Organization organization, MatchingContext context) {
        List<UUID> currentMatchesIds = findCurrentMatches(organization).stream()
            .map(m -> m.getPartnerVersion().getId())
            .collect(Collectors.toList());
        List<Organization> notMatchedOrgs = findNotMatchedOrgs(currentMatchesIds, organization.getAccount().getName());
        findAndPersistMatches(organization, notMatchedOrgs, context);
        detectConflictsForCurrentMatches(organization);
    }

    private void detectConflictsForCurrentMatches(Organization organization) {
        List<OrganizationMatch> matches = findCurrentMatches(organization);
        matches.addAll(findCurrentPartnersMatches(organization));
        conflictDetectionService.detect(matches);
    }

    private List<OrganizationMatch> findCurrentMatches(Organization organization) {
        return organizationMatchRepository
            .findAllByOrganizationRecordId(organization.getId());
    }

    private List<OrganizationMatch> findCurrentPartnersMatches(Organization organization) {
        return organizationMatchRepository
            .findAllByPartnerVersionId(organization.getId());
    }

    private List<Organization> findNotMatchedOrgs(List<UUID> currentMatchesIds, String providerName) {
        return organizationService.findAllOthers(providerName).stream()
            .filter(o -> !currentMatchesIds.contains(o.getId()))
            .collect(Collectors.toList());
    }

    private List<OrganizationMatch> findAndPersistMatches(Organization organization, List<Organization> notMatchedOrgs,
                                                          MatchingContext context) {
        List<OrganizationMatch> matches = new LinkedList<>();
        long startTime = System.currentTimeMillis();
        //TODO: Remove time counting logic (#264)
        log.debug("Searching for matches for " + organization.getAccount().getName() + "'s organization '" +
            organization.getName() + "' has started. There are " + notMatchedOrgs.size() + " organizations to compare with");
        for (Organization partner : notMatchedOrgs) {
            if (isSimilar(organization, partner, context)) {
                matches.addAll(createOrganizationMatches(organization, partner));
            }
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        //TODO: Remove time counting logic (#264)
        log.debug("Searching for matches for " +
            organization.getAccount().getName() + "'s organization '" +
            organization.getName() + "' took: " + elapsedTime + "ms, " + matches.size() + " matches found.");
        return matches;
    }

    private List<OrganizationMatch> createOrganizationMatches(Organization organization, Organization partner) {
        List<OrganizationMatch> matches = new LinkedList<>();
        OrganizationMatch match = new OrganizationMatch()
            .organizationRecord(organization)
            .partnerVersion(partner)
            .timestamp(ZonedDateTime.now());

        OrganizationMatch mirrorMatch = new OrganizationMatch()
            .organizationRecord(partner)
            .partnerVersion(organization)
            .timestamp(ZonedDateTime.now());

        matches.add(save(match));
        matches.add(save(mirrorMatch));

        return matches;
    }

    private boolean isSimilar(Organization organization, Organization partner, MatchingContext context) {
        return organizationSimilarityCounter.countSimilarityRatio(organization, partner, context) >= orgMatchThreshold;
    }
}
