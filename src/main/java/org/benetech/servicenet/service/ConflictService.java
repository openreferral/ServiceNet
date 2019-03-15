package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.service.dto.ConflictDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Conflict.
 */
public interface ConflictService {

    /**
     * Save a conflict.
     *
     * @param conflictDTO the entity to save
     * @return the persisted entity
     */
    ConflictDTO save(ConflictDTO conflictDTO);

    /**
     * Get all the conflicts.
     *
     * @return the list of entities
     */
    List<ConflictDTO> findAll();

    /**
     * Get all the Conflict with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<ConflictDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" conflict.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ConflictDTO> findOne(UUID id);

    /**
     * Delete the "id" conflict.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * Get all the Conflict with resourceId and ownerId.
     *
     * @param resourceId the id of the resource entity
     * @param ownerId the id of the owner entity
     */
    List<ConflictDTO> findAllWithResourceIdAndOwnerId(UUID resourceId, UUID ownerId);

    /**
     * Get all the Conflict with resourceId.
     *
     * @param resourceId the id of the resource entity
     */
    List<ConflictDTO> findAllPendingWithResourceId(UUID resourceId);

    /**
     * Get max offeredValueDate of Conflict with resourceId.
     *
     * @param resourceId the id of the resource entity
     */
    Optional<ZonedDateTime> findMostRecentStateDate(UUID resourceId);

    /**
     * Get list of pending conflicts wit specified resourceId, fieldName and offeredValue.
     *
     * @param resourceId the id of the resource entity
     * @param fieldName the currentValue of the resource entity
     * @param offeredValue the offeredValue of the resource entity
     */
    List<Conflict> findAllConflictsWhichOffersTheSameValue(UUID resourceId,
                                                           String fieldName,
                                                           String offeredValue);

}
