package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.DocumentUploadDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing DocumentUpload.
 */
public interface DocumentUploadService {

    /**
     * Save a documentUpload.
     *
     * @param documentUploadDTO the entity to save
     * @return the persisted entity
     */
    DocumentUploadDTO save(DocumentUploadDTO documentUploadDTO);

    /**
     * Get all the documentUploads.
     *
     * @return the list of entities
     */
    List<DocumentUploadDTO> findAll();


    /**
     * Get the "id" documentUpload.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DocumentUploadDTO> findOne(UUID id);

    /**
     * Delete the "id" documentUpload.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
