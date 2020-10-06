package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.ServiceAreaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the serviceAreas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ServiceAreaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" serviceArea.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ServiceAreaDTO> findOne(UUID id);

    /**
     * Delete the "id" serviceArea.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
