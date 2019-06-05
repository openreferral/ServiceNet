package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.ShelterDTO;

/**
 * Service Interface for managing Shelter.
 */
public interface ShelterService {

    /**
     * Save a shelter.
     *
     * @param shelterDto the entity to save
     * @return the persisted entity
     */
    ShelterDTO save(ShelterDTO shelterDto);

    /**
     * Get all the shelters.
     *
     * @return the list of entities
     */
    List<ShelterDTO> findAll();


    /**
     * Get the "id" shelter.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ShelterDTO> findOne(UUID id);

    /**
     * Delete the "id" shelter.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
