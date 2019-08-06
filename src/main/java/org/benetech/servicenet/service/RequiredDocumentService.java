package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.service.dto.RequiredDocumentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the requiredDocuments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RequiredDocumentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" requiredDocument.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RequiredDocumentDTO> findOne(UUID id);

    Optional<RequiredDocument> findForExternalDb(String externalDbId, String providerName);

    /**
     * Delete the "id" requiredDocument.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
