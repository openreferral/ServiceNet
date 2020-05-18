package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.domain.DailyUpdate;
import org.benetech.servicenet.service.dto.DailyUpdateDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.DailyUpdate}.
 */
public interface DailyUpdateService {

    /**
     * Save a dailyUpdate.
     *
     * @param dailyUpdateDTO the entity to save.
     * @return the persisted entity.
     */
    DailyUpdateDTO save(DailyUpdateDTO dailyUpdateDTO);

    /**
     * Save a dailyUpdate.
     *
     * @param dailyUpdate the entity to save.
     * @return the persisted entity.
     */
    DailyUpdate save(DailyUpdate dailyUpdate);

    /**
     * Get all the dailyUpdates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DailyUpdateDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dailyUpdate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DailyUpdateDTO> findOne(UUID id);

    /**
     * Delete the "id" dailyUpdate.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
