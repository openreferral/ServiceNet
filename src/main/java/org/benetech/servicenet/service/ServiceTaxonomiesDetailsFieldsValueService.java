package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.service.dto.ServiceTaxonomiesDetailsFieldsValueDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.ServiceTaxonomiesDetailsFieldsValue}.
 */
public interface ServiceTaxonomiesDetailsFieldsValueService {

    /**
     * Save a serviceTaxonomiesDetailsFieldsValue.
     *
     * @param serviceTaxonomiesDetailsFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    ServiceTaxonomiesDetailsFieldsValueDTO save(
        ServiceTaxonomiesDetailsFieldsValueDTO serviceTaxonomiesDetailsFieldsValueDTO
    );

    /**
     * Get all the serviceTaxonomiesDetailsFieldsValues.
     *
     * @return the list of entities.
     */
    List<ServiceTaxonomiesDetailsFieldsValueDTO> findAll();


    /**
     * Get the "id" serviceTaxonomiesDetailsFieldsValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceTaxonomiesDetailsFieldsValueDTO> findOne(UUID id);

    /**
     * Delete the "id" serviceTaxonomiesDetailsFieldsValue.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
