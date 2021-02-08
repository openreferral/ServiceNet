package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.service.dto.ServiceAtLocationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Save a serviceAtLocation.
     *
     * @param serviceAtLocation the entity to save
     * @return the persisted entity
     */
    ServiceAtLocation save(ServiceAtLocation serviceAtLocation);

    /**
     * Get all the serviceAtLocations.
     *
     * @return the list of entities
     */
    List<ServiceAtLocationDTO> findAll();

    /**
     * Get all the serviceAtLocations on page
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ServiceAtLocationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" serviceAtLocation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ServiceAtLocationDTO> findOne(UUID id);

    Optional<ServiceAtLocation> findForExternalDb(String externalDbId, String providerName);

    List<ServiceAtLocation> findByLocation(Location location);

    /**
     * Delete the "id" serviceAtLocation.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
