package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.ActivityDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Activity.
 */
public interface ActivityService {

    /**
     * Save a activity.
     *
     * @param activityDTO the entity to save
     * @return the persisted entity
     */
    ActivityDTO save(ActivityDTO activityDTO);

    /**
     * Get all the activities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ActivityDTO> findAll(Pageable pageable);

    /**
     * Get all the Activity with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<ActivityDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" activity.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ActivityDTO> findOne(UUID id);

    /**
     * Delete the "id" activity.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
