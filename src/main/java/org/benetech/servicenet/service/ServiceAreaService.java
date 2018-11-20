package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.ServiceAreaDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ServiceArea.
 */
public interface ServiceAreaService {

    /**
     * Save a serviceArea.
     *
     * @param serviceAreaDTO the entity to save
     * @return the persisted entity
     */
    ServiceAreaDTO save(ServiceAreaDTO serviceAreaDTO);

    /**
     * Get all the serviceAreas.
     *
     * @return the list of entities
     */
    List<ServiceAreaDTO> findAll();


    /**
     * Get the "id" serviceArea.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ServiceAreaDTO> findOne(Long id);

    /**
     * Delete the "id" serviceArea.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
