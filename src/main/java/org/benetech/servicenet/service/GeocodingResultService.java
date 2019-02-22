package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.GeocodingResultDTO;

import java.util.List;
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
     * @return the list of entities
     */
    List<GeocodingResultDTO> findAll();

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
