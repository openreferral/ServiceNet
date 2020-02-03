package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.service.dto.OrganizationFieldsValueDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.OrganizationFieldsValue}.
 */
public interface OrganizationFieldsValueService {

    /**
     * Save a organizationFieldsValue.
     *
     * @param organizationFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationFieldsValueDTO save(OrganizationFieldsValueDTO organizationFieldsValueDTO);

    /**
     * Get all the organizationFieldsValues.
     *
     * @return the list of entities.
     */
    List<OrganizationFieldsValueDTO> findAll();


    /**
     * Get the "id" organizationFieldsValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationFieldsValueDTO> findOne(UUID id);

    /**
     * Delete the "id" organizationFieldsValue.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
