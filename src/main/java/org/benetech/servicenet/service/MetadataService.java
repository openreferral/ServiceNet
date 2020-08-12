package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.service.dto.MetadataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Metadata.
 */
public interface MetadataService {

    /**
     * Save a metadata.
     *
     * @param metadataDTO the entity to save
     * @return the persisted entity
     */
    MetadataDTO save(MetadataDTO metadataDTO);

    /**
     * Save all metadata objects, with current user reference, or with the system user reference if no use is logged in
     *
     * @param metadata the entities to save
     * @return the persisted entities
     */
    List<Metadata> saveForCurrentOrSystemUser(List<Metadata> metadata);

    /**
     * Get all the metadata.
     *
     * @return the list of entities
     */
    List<MetadataDTO> findAll();

    /**
     * Get all the metadata.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MetadataDTO> findAll(Pageable pageable);

    /**
     * Get the "id" metadata.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MetadataDTO> findOne(UUID id);

    /**
     * Delete the "id" metadata.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * Get most recent metadata for specific change of entity with resourceId.
     *
     * @param resourceId the resourceId of the entity
     * @param fieldName the field name of the entity
     * @param replacementValue the value of changed field of the entity
     *
     * @return a metadata related to field change or to creation of the whole entity.
     */
    Optional<Metadata> findMetadataForConflict(UUID resourceId, String fieldName, String replacementValue);
}
