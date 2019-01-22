package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.ConfidentialRecordDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing ConfidentialRecord.
 */
public interface ConfidentialRecordService {

    /**
     * Save a confidentialRecord.
     *
     * @param confidentialRecordDTO the entity to save
     * @return the persisted entity
     */
    ConfidentialRecordDTO save(ConfidentialRecordDTO confidentialRecordDTO);

    /**
     * Get all the confidentialRecords.
     *
     * @return the list of entities
     */
    List<ConfidentialRecordDTO> findAll();


    /**
     * Get the "id" confidentialRecord.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ConfidentialRecordDTO> findOne(UUID id);

    /**
     * Delete the "id" confidentialRecord.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
