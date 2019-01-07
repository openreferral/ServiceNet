package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.service.dto.LocationDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Location.
 */
public interface LocationService {

    /**
     * Save a location.
     *
     * @param locationDTO the entity to save
     * @return the persisted entity
     */
    LocationDTO save(LocationDTO locationDTO);

    Location save(Location location);

    /**
     * Get all the locations.
     *
     * @return the list of entities
     */
    List<LocationDTO> findAll();

    /**
     * Get all the LocationDTO where PhysicalAddress is null.
     *
     * @return the list of entities
     */
    List<LocationDTO> findAllWherePhysicalAddressIsNull();

    /**
     * Get all the LocationDTO where PostalAddress is null.
     *
     * @return the list of entities
     */
    List<LocationDTO> findAllWherePostalAddressIsNull();

    /**
     * Get all the LocationDTO where RegularSchedule is null.
     *
     * @return the list of entities
     */
    List<LocationDTO> findAllWhereRegularScheduleIsNull();

    /**
     * Get all the LocationDTO where HolidaySchedule is null.
     *
     * @return the list of entities
     */
    List<LocationDTO> findAllWhereHolidayScheduleIsNull();

    Optional<Location> findForExternalDb(String externalDbId, String providerName);

    /**
     * Get the "id" location.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LocationDTO> findOne(UUID id);

    /**
     * Delete the "id" location.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
