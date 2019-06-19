package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.LocationExclusionDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.LocationExclusion}.
 */
public interface LocationExclusionService {

    /**
     * Save a locationExclusion.
     *
     * @param locationExclusionDTO the entity to save.
     * @return the persisted entity.
     */
    LocationExclusionDTO save(LocationExclusionDTO locationExclusionDTO);

    /**
     * Get all the locationExclusions.
     *
     * @return the list of entities.
     */
    List<LocationExclusionDTO> findAll();


    /**
     * Get the "id" locationExclusion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LocationExclusionDTO> findOne(UUID id);

    /**
     * Delete the "id" locationExclusion.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
