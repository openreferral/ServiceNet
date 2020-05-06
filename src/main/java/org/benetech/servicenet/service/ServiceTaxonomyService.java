package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.benetech.servicenet.service.dto.ServiceTaxonomyDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
     * Save a serviceTaxonomy.
     *
     * @param serviceTaxonomy the entity to save
     * @return the persisted entity
     */
    ServiceTaxonomy save(ServiceTaxonomy serviceTaxonomy);

    /**
     * Get all the serviceTaxonomies.
     *
     * @return the list of entities
     */
    List<ServiceTaxonomyDTO> findAll();

    /**
     * Get all the serviceTaxonomies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ServiceTaxonomyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" serviceTaxonomy.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ServiceTaxonomyDTO> findOne(UUID id);

    Optional<ServiceTaxonomy> findForExternalDb(String externalDbId, String providerName);

    /**
     * Delete the "id" serviceTaxonomy.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
