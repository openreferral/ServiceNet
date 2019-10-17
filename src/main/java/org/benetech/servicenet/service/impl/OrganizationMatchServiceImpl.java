package org.benetech.servicenet.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.benetech.servicenet.conflict.ConflictDetectionService;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.matching.counter.OrganizationSimilarityCounter;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.benetech.servicenet.repository.MatchSimilarityRepository;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    private final MatchSimilarityRepository matchSimilarityRepository;

    private final BigDecimal orgMatchThreshold;

    @SuppressWarnings("checkstyle:ParameterNumber")
    public OrganizationMatchServiceImpl(OrganizationMatchRepository organizationMatchRepository,
                                        OrganizationMatchMapper organizationMatchMapper,
                                        OrganizationService organizationService,
                                        OrganizationSimilarityCounter organizationSimilarityCounter,
                                        ConflictDetectionService conflictDetectionService,
                                        UserService userService,
                                        MatchSimilarityService matchSimilarityService,
                                        MatchSimilarityRepository matchSimilarityRepository,
                                        @Value("${similarity-ratio.config.organization-match-threshold}")
                                            BigDecimal orgMatchThreshold) {
        this.organizationMatchRepository = organizationMatchRepository;
        this.organizationMatchMapper = organizationMatchMapper;
        this.organizationService = organizationService;
        this.organizationSimilarityCounter = organizationSimilarityCounter;
        this.conflictDetectionService = conflictDetectionService;
        this.userService = userService;
        this.orgMatchThreshold = orgMatchThreshold;
        this.matchSimilarityService = matchSimilarityService;
        this.matchSimilarityRepository = matchSimilarityRepository;
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
        organizationMatch = saveOrUpdate(organizationMatch);
        return organizationMatchMapper.toDto(organizationMatch);
    }

    public OrganizationMatch saveOrUpdate(OrganizationMatch organizationMatch) {
        Optional<OrganizationMatch> existingMatchOptional =
            organizationMatchRepository.findByOrganizationRecordAndPartnerVersion(
            organizationMatch.getOrganizationRecord(), organizationMatch.getPartnerVersion());
        if (existingMatchOptional.isPresent()) {
            OrganizationMatch existingMatch = existingMatchOptional.get();
            existingMatch.setTimestamp(organizationMatch.getTimestamp());
            existingMatch.setDismissed(organizationMatch.getDismissed());
            existingMatch.setDismissComment(organizationMatch.getDismissComment());
            existingMatch.setDismissDate(organizationMatch.getDismissDate());
            existingMatch.setDismissedBy(organizationMatch.getDismissedBy());
            existingMatch.setHidden(organizationMatch.getHidden());
            existingMatch.setHiddenBy(organizationMatch.getHiddenBy());
            existingMatch.setHiddenDate(organizationMatch.getHiddenDate());
            return organizationMatchRepository.save(existingMatch);
        } else {
            return organizationMatchRepository.save(organizationMatch);
        }
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

    /**
     * Get all the organizationMatches.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationMatchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrganizationMatches");
        return organizationMatchRepository.findAll(pageable)
            .map(organizationMatchMapper::toDto);
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

    @Override
    public List<OrganizationMatchDTO> findAllHiddenForOrganization(UUID orgId) {
        return organizationMatchRepository.findAllByOrganizationRecordIdAndHidden(orgId, true).stream()
            .map(organizationMatchMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<OrganizationMatchDTO> findAllNotHiddenForOrganization(UUID orgId) {
        return organizationMatchRepository.findAllByOrganizationRecordIdAndHidden(orgId, false).stream()
            .map(organizationMatchMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<OrganizationMatchDTO> findCurrentUsersHiddenOrganizationMatches() {
        Optional<User> currentUser = userService.getUserWithAuthoritiesAndAccount();
        if (currentUser.isPresent()) {
            List<OrganizationMatch> matches;
            if (currentUser.get().isAdmin()) {
                matches = organizationMatchRepository.findAllByHidden(true);
            } else {
                matches = organizationMatchRepository.findAllByHiddenAndHiddenBy(true, currentUser.get());
            }
            return matches.stream()
                .map(organizationMatchMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        } else {
            throw new IllegalStateException("No current user found");
        }
    }

    @Override
    public List<OrganizationMatchDTO> findAllNotHiddenOrganizationMatches() {
        return organizationMatchRepository.findAllByHidden(false).stream()
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
        log.info(organization.getName() + ": Updating organization matches");
        List<OrganizationMatch> matches = findCurrentMatches(organization);
        List<OrganizationMatch> partnerMatches = findCurrentPartnersMatches(organization);
        if (organization.getActive()) {
            List<UUID> hiddenMatchesIds = matches.stream()
                .filter(m -> BooleanUtils.isTrue((m.getHidden())))
                .map(OrganizationMatch::getId)
                .collect(Collectors.toList());

            for (UUID matchId : hiddenMatchesIds) {
                revertHideOrganizationMatch(matchId);
            }
            List<Organization> partnerOrganizations = findOrganizationsExcept(organization.getAccount().getName());

            List<OrganizationMatch> currentMatches = findAndPersistMatches(organization, partnerOrganizations, context);
            removeObsoleteMatches(currentMatches, matches);
            removeObsoleteMatches(currentMatches, partnerMatches);

            detectConflictsForCurrentMatches(organization);
        } else {
            removeMatches(matches);
            removeMatches(partnerMatches);
        }
    }

    private void removeObsoleteMatches(List<OrganizationMatch> matches, List<OrganizationMatch> previousMatches) {
        List<OrganizationMatch> matchesToRemove = new ArrayList<>();
        for (OrganizationMatch match : previousMatches) {
            if (!matches.contains(match)) {
                matchesToRemove.add(match);
            }
        }
        removeMatches(matchesToRemove);
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

            conflictDetectionService.detectAsynchronously(match.getOrganizationRecord(), Collections.singletonList(match));
        });
    }

    @Override
    public void hideOrganizationMatch(UUID id) {
        organizationMatchRepository.findById(id).ifPresent(match -> {
            match.setHidden(true);
            match.setHiddenDate(ZonedDateTime.now(ZoneId.systemDefault()));

            userService.getUserWithAuthoritiesAndAccount().ifPresentOrElse(
                match::setHiddenBy,
                () -> { throw new IllegalStateException("No current user found"); }
            );

            organizationMatchRepository.save(match);
        });
    }

    @Override
    public void hideOrganizationMatches(List<UUID> ids) {
        for (UUID id : ids) {
            hideOrganizationMatch(id);
        }
    }

    @Override
    public void revertHideOrganizationMatch(UUID id) {
        Optional<User> currentUser = userService.getUserWithAuthoritiesAndAccount();
        if (currentUser.isPresent()) {
            organizationMatchRepository.findById(id).ifPresent(match -> {
                if (currentUser.get().isAdmin() || match.getHiddenBy().equals(currentUser.get())) {
                    match.setHidden(false);
                    match.setHiddenBy(null);
                    match.setHiddenDate(null);

                    organizationMatchRepository.save(match);
                } else {
                    throw new AccessDeniedException("Cannot reinstate matches hidden by another user");
                }
            });
        } else {
            throw new IllegalStateException("No current user found");
        }
    }

    private void detectConflictsForCurrentMatches(Organization organization) {
        List<OrganizationMatch> matches = findNotDismissedMatches(organization);
        matches.addAll(findNotDismissedPartnersMatches(organization));
        conflictDetectionService.detect(organization, matches);
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
        return organizationService.findAllOthersExcept(providerName, currentMatchesIds);
    }

    private List<Organization> findOrganizationsExcept(String providerName) {
        return organizationService.findAllOthersExcept(providerName, new ArrayList<>());
    }

    private List<OrganizationMatch> findNotHiddenMatches(Organization organization) {
        return organizationMatchRepository
            .findAllByOrganizationRecordIdAndHidden(organization.getId(), false);
    }

    private List<OrganizationMatch> findHiddenMatches(Organization organization) {
        return organizationMatchRepository
            .findAllByOrganizationRecordIdAndHidden(organization.getId(), true);
    }

    private List<OrganizationMatch> findAndPersistMatches(Organization organization,
        List<Organization> partnerOrganizations, MatchingContext context) {
        List<OrganizationMatch> matches = new LinkedList<>();
        long startTime = System.currentTimeMillis();
        //TODO: Remove time counting logic (#264)
        log.debug("Searching for matches for " + organization.getAccount().getName() + "'s organization '" +
            organization.getName() + "' has started. There are "
            + partnerOrganizations.size() + " organizations to compare with");
        for (Organization partner : partnerOrganizations) {
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

    private void removeMatches(List<OrganizationMatch> matches) {
        for (OrganizationMatch match : matches) {
            matchSimilarityRepository.deleteAll(
                matchSimilarityRepository.findByOrganizationMatchId(match.getId())
            );
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

        matches.add(saveOrUpdate(match));
        matches.add(saveOrUpdate(mirrorMatch));

        for (MatchSimilarityDTO similarityDTO : similarityDTOS) {
            similarityDTO.setOrganizationMatchId(match.getId());
            matchSimilarityService.saveOrUpdate(similarityDTO);
            similarityDTO.setOrganizationMatchId(mirrorMatch.getId());
            matchSimilarityService.saveOrUpdate(similarityDTO);
        }
        return matches;
    }

    private boolean isSimilar(List<MatchSimilarityDTO> similarityDTOS) {
        return similarityDTOS.stream()
            .map(MatchSimilarityDTO::getSimilarity)
            .reduce(BigDecimal.ZERO, BigDecimal::add).compareTo(orgMatchThreshold) >= 0;
    }
}
