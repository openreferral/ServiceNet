package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Taxonomy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.benetech.servicenet.service.dto.TaxonomyDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Taxonomy.
 */
public interface TaxonomyService {

    /**
     * Save a taxonomy.
     *
     * @param taxonomyDTO the entity to save
     * @return the persisted entity
     */
    TaxonomyDTO save(TaxonomyDTO taxonomyDTO);

    /**
     * Get all the taxonomies.
     *
     * @return the list of entities
     */
    List<TaxonomyDTO> findAll();

    /**
     * Get all the taxonomies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TaxonomyDTO> findAll(Pageable pageable);

    /**
     * Get associated taxonomies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TaxonomyDTO> findAssociatedTaxonomies(Pageable pageable);

    /**
     * Get the "id" taxonomy.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TaxonomyDTO> findOne(UUID id);

    Optional<Taxonomy> findForExternalDb(String externalDbId, String providerName);

    Optional<Taxonomy> findForTaxonomyId(String taxonomyId, String providerName);

    Taxonomy save(Taxonomy taxonomy);

    /**
     * Delete the "id" taxonomy.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
