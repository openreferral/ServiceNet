package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.service.dto.LocationFieldsValueDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.LocationFieldsValue}.
 */
public interface LocationFieldsValueService {

    /**
     * Save a locationFieldsValue.
     *
     * @param locationFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    LocationFieldsValueDTO save(LocationFieldsValueDTO locationFieldsValueDTO);

    /**
     * Get all the locationFieldsValues.
     *
     * @return the list of entities.
     */
    List<LocationFieldsValueDTO> findAll();


    /**
     * Get the "id" locationFieldsValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LocationFieldsValueDTO> findOne(UUID id);

    /**
     * Delete the "id" locationFieldsValue.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
