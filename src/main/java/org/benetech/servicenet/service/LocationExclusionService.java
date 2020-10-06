package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.LocationExclusionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the locationExclusions.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    Page<LocationExclusionDTO> findAll(Pageable pageable);

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
