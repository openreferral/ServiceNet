package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.RequiredDocumentDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing RequiredDocument.
 */
public interface RequiredDocumentService {

    /**
     * Save a requiredDocument.
     *
     * @param requiredDocumentDTO the entity to save
     * @return the persisted entity
     */
    RequiredDocumentDTO save(RequiredDocumentDTO requiredDocumentDTO);

    /**
     * Get all the requiredDocuments.
     *
     * @return the list of entities
     */
    List<RequiredDocumentDTO> findAll();


    /**
     * Get the "id" requiredDocument.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RequiredDocumentDTO> findOne(UUID id);

    /**
     * Delete the "id" requiredDocument.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
