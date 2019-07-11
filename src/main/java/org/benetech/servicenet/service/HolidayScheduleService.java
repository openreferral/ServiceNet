package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.service.dto.HolidayScheduleDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing HolidaySchedule.
 */
public interface HolidayScheduleService {

    /**
     * Save a holidaySchedule.
     *
     * @param holidayScheduleDTO the entity to save
     * @return the persisted entity
     */
    HolidayScheduleDTO save(HolidayScheduleDTO holidayScheduleDTO);

    /**
     * Get all the holidaySchedules.
     *
     * @return the list of entities
     */
    List<HolidayScheduleDTO> findAll();


    /**
     * Get the "id" holidaySchedule.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<HolidayScheduleDTO> findOne(UUID id);

    Optional<HolidaySchedule> findForExternalDb(String externalDbId, String providerName);

    /**
     * Delete the "id" holidaySchedule.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
