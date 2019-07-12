package org.benetech.servicenet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.conflict.ConflictDetectionService;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.matching.counter.OrganizationSimilarityCounter;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.benetech.servicenet.repository.OrganizationMatchRepository;
import org.benetech.servicenet.service.MatchSimilarityService;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.DismissMatchDTO;
import org.benetech.servicenet.service.dto.MatchSimilarityDTO;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.benetech.servicenet.service.mapper.OrganizationMatchMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
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

    private final UserService userService;

    private final MatchSimilarityService matchSimilarityService;

    private final float orgMatchThreshold;

    public OrganizationMatchServiceImpl(OrganizationMatchRepository organizationMatchRepository,
                                        OrganizationMatchMapper organizationMatchMapper,
                                        OrganizationService organizationService,
                                        OrganizationSimilarityCounter organizationSimilarityCounter,
                                        ConflictDetectionService conflictDetectionService,
                                        UserService userService,
                                        MatchSimilarityService matchSimilarityService,
                                        @Value("${similarity-ratio.config.organization-match-threshold}")
                                            float orgMatchThreshold) {
        this.organizationMatchRepository = organizationMatchRepository;
        this.organizationMatchMapper = organizationMatchMapper;
        this.organizationService = organizationService;
        this.organizationSimilarityCounter = organizationSimilarityCounter;
        this.conflictDetectionService = conflictDetectionService;
        this.userService = userService;
        this.orgMatchThreshold = orgMatchThreshold;
        this.matchSimilarityService = matchSimilarityService;
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

    @Override
    public List<OrganizationMatchDTO> findAllDismissedForOrganization(UUID orgId) {
        return organizationMatchRepository.findAllByOrganizationRecordIdAndDismissed(orgId, true).stream()
            .map(organizationMatchMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<OrganizationMatchDTO> findAllNotDismissedForOrganization(UUID orgId) {
        return organizationMatchRepository.findAllByOrganizationRecordIdAndDismissed(orgId, false).stream()
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
        if (organization.getActive()) {
            List<UUID> currentMatchesIds = findCurrentMatches(organization).stream()
                .map(m -> m.getPartnerVersion().getId())
                .collect(Collectors.toList());
            List<Organization> notMatchedOrgs = findNotMatchedOrgs(currentMatchesIds, organization.getAccount().getName());
            findAndPersistMatches(organization, notMatchedOrgs, context);
            detectConflictsForCurrentMatches(organization);
        } else {
            removeMatches(findCurrentMatches(organization));
            removeMatches(findCurrentPartnersMatches(organization));
        }
    }

    @Override
    public void dismissOrganizationMatch(UUID id, DismissMatchDTO dismissMatchDTO) {
        organizationMatchRepository.findById(id).ifPresent(match -> {
            match.setDismissed(true);
            match.setDismissComment(dismissMatchDTO.getComment());
            match.setDismissDate(ZonedDateTime.now(ZoneId.systemDefault()));

            userService.getUserWithAuthoritiesAndAccount().ifPresentOrElse(
                match::setDismissedBy,
                () -> { throw new IllegalStateException("No current user found"); }
            );

            organizationMatchRepository.save(match);

            conflictDetectionService.remove(match);
        });
    }

    @Override
    public void revertDismissOrganizationMatch(UUID id) {
        organizationMatchRepository.findById(id).ifPresent(match -> {
            match.setDismissed(false);
            match.setDismissComment(null);
            match.setDismissedBy(null);
            match.setDismissDate(null);

            organizationMatchRepository.save(match);

            conflictDetectionService.detect(Collections.singletonList(match));
        });
    }

    private void detectConflictsForCurrentMatches(Organization organization) {
        List<OrganizationMatch> matches = findNotDismissedMatches(organization);
        matches.addAll(findNotDismissedPartnersMatches(organization));
        conflictDetectionService.detect(matches);
    }

    private List<OrganizationMatch> findCurrentMatches(Organization organization) {
        return organizationMatchRepository
            .findAllByOrganizationRecordId(organization.getId());
    }

    private List<OrganizationMatch> findNotDismissedMatches(Organization organization) {
        return organizationMatchRepository
            .findAllByOrganizationRecordIdAndDismissed(organization.getId(), false);
    }

    private List<OrganizationMatch> findCurrentPartnersMatches(Organization organization) {
        return organizationMatchRepository
            .findAllByPartnerVersionId(organization.getId());
    }

    private List<OrganizationMatch> findNotDismissedPartnersMatches(Organization organization) {
        return organizationMatchRepository
            .findAllByPartnerVersionIdAndDismissed(organization.getId(), false);
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
            List<MatchSimilarityDTO> similarityDTOs = organizationSimilarityCounter
                .getMatchSimilarityDTOs(organization, partner, context);
            if (isSimilar(similarityDTOs)) {
                matches.addAll(createOrganizationMatches(organization, partner, similarityDTOs));
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

    private void removeMatches(List<OrganizationMatch>  matches) {
        for (OrganizationMatch match : matches) {
            conflictDetectionService.remove(match);
            organizationMatchRepository.delete(match);
        }
    }

    private List<OrganizationMatch> createOrganizationMatches(Organization organization, Organization partner,
        List<MatchSimilarityDTO> similarityDTOS) {
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

        for (MatchSimilarityDTO similarityDTO : similarityDTOS) {
            similarityDTO.setOrganizationMatchId(match.getId());
            matchSimilarityService.save(similarityDTO);
            similarityDTO.setOrganizationMatchId(mirrorMatch.getId());
            matchSimilarityService.save(similarityDTO);
        }
        return matches;
    }

    private boolean isSimilar(List<MatchSimilarityDTO> similarityDTOS) {
        float start = 0;
        return similarityDTOS.stream()
            .map(MatchSimilarityDTO::getSimilarity)
            .reduce(start, Float::sum) >= orgMatchThreshold;
    }
}
