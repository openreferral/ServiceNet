package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.ServiceDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Service.
 */
public interface ServiceService {

    /**
     * Save a service.
     *
     * @param serviceDTO the entity to save
     * @return the persisted entity
     */
    ServiceDTO save(ServiceDTO serviceDTO);

    /**
     * Get all the services.
     *
     * @return the list of entities
     */
    List<ServiceDTO> findAll();

    /**
     * Get all the ServiceDTO where Location is null.
     *
     * @return the list of entities
     */
    List<ServiceDTO> findAllWhereLocationIsNull();

    /**
     * Get all the ServiceDTO where RegularSchedule is null.
     *
     * @return the list of entities
     */
    List<ServiceDTO> findAllWhereRegularScheduleIsNull();

    /**
     * Get all the ServiceDTO where HolidaySchedule is null.
     *
     * @return the list of entities
     */
    List<ServiceDTO> findAllWhereHolidayScheduleIsNull();

    /**
     * Get all the ServiceDTO where Funding is null.
     *
     * @return the list of entities
     */
    List<ServiceDTO> findAllWhereFundingIsNull();

    /**
     * Get all the ServiceDTO where Eligibility is null.
     *
     * @return the list of entities
     */
    List<ServiceDTO> findAllWhereEligibilityIsNull();


    /**
     * Get the "id" service.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ServiceDTO> findOne(UUID id);

    /**
     * Delete the "id" service.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
