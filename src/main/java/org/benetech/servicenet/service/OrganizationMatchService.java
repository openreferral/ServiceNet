package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.service.dto.DismissMatchDTO;
import org.benetech.servicenet.service.dto.MatchSimilarityDTO;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing OrganizationMatch.
 */
public interface OrganizationMatchService {

    /**
     * Save a organizationMatch.
     *
     * @param organizationMatchDTO the entity to save
     * @return the persisted entity
     */
    OrganizationMatchDTO save(OrganizationMatchDTO organizationMatchDTO);

    OrganizationMatch saveOrUpdate(OrganizationMatch organizationMatch);

    /**
     * Get all the organizationMatches.
     *
     * @return the list of entities
     */
    List<OrganizationMatchDTO> findAll();

    List<OrganizationMatchDTO> findAllForOrganization(UUID orgId);

    List<OrganizationMatch> findAllMatchesForOrganization(UUID orgId);

    List<OrganizationMatchDTO> findAllDismissedForOrganization(UUID orgId);

    List<OrganizationMatchDTO> findAllNotDismissedForOrganization(UUID orgId);

    List<OrganizationMatchDTO> findAllHiddenForOrganization(UUID orgId);

    List<OrganizationMatchDTO> findAllNotHiddenForOrganization(UUID orgId);

    List<OrganizationMatchDTO> findCurrentUsersHiddenOrganizationMatches();

    List<OrganizationMatchDTO> findAllNotHiddenOrganizationMatches();

    /**
     * Get all the organizationMatches.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OrganizationMatchDTO> findAll(Pageable pageable);

    /**
     * Get the "id" organizationMatch.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrganizationMatchDTO> findOne(UUID id);

    /**
     * Delete the "id" organizationMatch.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    void createOrUpdateOrganizationMatches(Organization organization);

    void createOrUpdateOrganizationMatchesSynchronously(Organization organization);

    void dismissOrganizationMatch(UUID id, DismissMatchDTO dismissMatchDTO);

    void revertDismissOrganizationMatch(UUID id);

    void hideOrganizationMatch(UUID id);

    void hideOrganizationMatches(List<UUID> ids);

    void revertHideOrganizationMatch(UUID id);

    List<OrganizationMatch> createOrganizationMatches(Organization organization, Organization partner,
        List<MatchSimilarityDTO> similarityDTOS);
}
