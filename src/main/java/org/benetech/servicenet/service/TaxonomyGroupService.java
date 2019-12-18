package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.service.dto.TaxonomyGroupDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.TaxonomyGroup}.
 */
public interface TaxonomyGroupService {

    /**
     * Save a taxonomyGroup.
     *
     * @param taxonomyGroupDTO the entity to save.
     * @return the persisted entity.
     */
    TaxonomyGroupDTO save(TaxonomyGroupDTO taxonomyGroupDTO);

    /**
     * Get all the taxonomyGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxonomyGroupDTO> findAll(Pageable pageable);

    /**
     * Get all the taxonomyGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<TaxonomyGroupDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" taxonomyGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaxonomyGroupDTO> findOne(UUID id);

    /**
     * Delete the "id" taxonomyGroup.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
