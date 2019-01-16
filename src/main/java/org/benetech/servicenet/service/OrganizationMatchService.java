package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;

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

    OrganizationMatch save(OrganizationMatch organizationMatch);

    /**
     * Get all the organizationMatches.
     *
     * @return the list of entities
     */
    List<OrganizationMatchDTO> findAll();

    List<OrganizationMatchDTO> findAllForOrganization(UUID orgId);

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
}
