package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.service.dto.ServiceAtLocationDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing ServiceAtLocation.
 */
public interface ServiceAtLocationService {

    /**
     * Save a serviceAtLocation.
     *
     * @param serviceAtLocationDTO the entity to save
     * @return the persisted entity
     */
    ServiceAtLocationDTO save(ServiceAtLocationDTO serviceAtLocationDTO);

    /**
     * Get all the serviceAtLocations.
     *
     * @return the list of entities
     */
    List<ServiceAtLocationDTO> findAll();

    /**
     * Get the "id" serviceAtLocation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ServiceAtLocationDTO> findOne(UUID id);

    Optional<ServiceAtLocation> findForExternalDb(String externalDbId, String providerName);

    /**
     * Delete the "id" serviceAtLocation.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
