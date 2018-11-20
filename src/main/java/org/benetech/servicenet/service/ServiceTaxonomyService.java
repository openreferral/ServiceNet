package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.ServiceTaxonomyDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ServiceTaxonomy.
 */
public interface ServiceTaxonomyService {

    /**
     * Save a serviceTaxonomy.
     *
     * @param serviceTaxonomyDTO the entity to save
     * @return the persisted entity
     */
    ServiceTaxonomyDTO save(ServiceTaxonomyDTO serviceTaxonomyDTO);

    /**
     * Get all the serviceTaxonomies.
     *
     * @return the list of entities
     */
    List<ServiceTaxonomyDTO> findAll();


    /**
     * Get the "id" serviceTaxonomy.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ServiceTaxonomyDTO> findOne(Long id);

    /**
     * Delete the "id" serviceTaxonomy.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
