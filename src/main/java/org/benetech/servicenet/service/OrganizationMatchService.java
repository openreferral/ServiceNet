package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.OrganizationMatchDTO;

import java.util.List;
import java.util.Optional;

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

    /**
     * Get all the organizationMatches.
     *
     * @return the list of entities
     */
    List<OrganizationMatchDTO> findAll();


    /**
     * Get the "id" organizationMatch.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrganizationMatchDTO> findOne(Long id);

    /**
     * Delete the "id" organizationMatch.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
