package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.RegularScheduleDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing RegularSchedule.
 */
public interface RegularScheduleService {

    /**
     * Save a regularSchedule.
     *
     * @param regularScheduleDTO the entity to save
     * @return the persisted entity
     */
    RegularScheduleDTO save(RegularScheduleDTO regularScheduleDTO);

    /**
     * Get all the regularSchedules.
     *
     * @return the list of entities
     */
    List<RegularScheduleDTO> findAll();


    /**
     * Get the "id" regularSchedule.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RegularScheduleDTO> findOne(Long id);

    /**
     * Delete the "id" regularSchedule.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
