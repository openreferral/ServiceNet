package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.RegularScheduleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
     * Get all the regularSchedules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RegularScheduleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" regularSchedule.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RegularScheduleDTO> findOne(UUID id);

    /**
     * Delete the "id" regularSchedule.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
