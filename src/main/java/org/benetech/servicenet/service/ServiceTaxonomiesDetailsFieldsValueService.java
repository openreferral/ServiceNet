package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.ServiceTaxonomiesDetailsFieldsValueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the serviceTaxonomiesDetailsFieldsValues.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    Page<ServiceTaxonomiesDetailsFieldsValueDTO> findAll(Pageable pageable);

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
