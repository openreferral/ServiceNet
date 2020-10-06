package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.OrganizationFieldsValueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the organizationFieldsValues.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    Page<OrganizationFieldsValueDTO> findAll(Pageable pageable);

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
