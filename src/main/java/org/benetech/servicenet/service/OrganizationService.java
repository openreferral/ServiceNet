package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Organization.
 */
public interface OrganizationService {

    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save
     * @return the persisted entity
     */
    OrganizationDTO save(OrganizationDTO organizationDTO);

    /**
     * Get all the organizations.
     *
     * @return the list of entities
     */
    List<OrganizationDTO> findAllDTOs();

    List<Organization> findAll();

    List<Organization> findAllOthers(String providerName);

    /**
     * Get all the OrganizationDTO where Funding is null.
     *
     * @return the list of entities
     */
    List<OrganizationDTO> findAllWhereFundingIsNull();

    Optional<Organization> findWithEagerAssociations(String externalDbId, String providerName);

    /**
     * Get the "id" organization.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrganizationDTO> findOneDTO(UUID id);

    Optional<Organization> findOne(UUID id);

    /**
     * Delete the "id" organization.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<UUID> findAllOrgIdsWithOwnerId(UUID ownerId, Pageable pageable);

    List<OrganizationDTO> findAllWithOwnerId(UUID ownerId);

    Page<Organization> findAllWithOwnerId(UUID ownerId, Pageable pageable);
}
