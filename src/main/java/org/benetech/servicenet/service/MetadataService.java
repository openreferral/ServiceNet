package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.service.dto.MetadataDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
}
