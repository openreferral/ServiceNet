package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.GeocodingResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing GeocodingResult.
 */
public interface GeocodingResultService {

    /**
     * Save a geocodingResult.
     *
     * @param geocodingResultDTO the entity to save
     * @return the persisted entity
     */
    GeocodingResultDTO save(GeocodingResultDTO geocodingResultDTO);

    /**
     * Get all the geocodingResults.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GeocodingResultDTO> findAll(Pageable pageable);


    /**
     * Get the "id" geocodingResult.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GeocodingResultDTO> findOne(UUID id);

    /**
     * Delete the "id" geocodingResult.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
