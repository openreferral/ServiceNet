package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing OpeningHours.
 */
public interface OpeningHoursService {

    /**
     * Save a openingHours.
     *
     * @param openingHoursDTO the entity to save
     * @return the persisted entity
     */
    OpeningHoursDTO save(OpeningHoursDTO openingHoursDTO);

    /**
     * Get all the openingHours.
     *
     * @return the list of entities
     */
    List<OpeningHoursDTO> findAll();

    /**
     * Get all the openingHours.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<OpeningHoursDTO> findAll(Pageable pageable);

    /**
     * Get the "id" openingHours.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OpeningHoursDTO> findOne(UUID id);

    /**
     * Delete the "id" openingHours.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
