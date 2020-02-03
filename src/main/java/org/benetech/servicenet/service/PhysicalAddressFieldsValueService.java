package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.service.dto.PhysicalAddressFieldsValueDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.PhysicalAddressFieldsValue}.
 */
public interface PhysicalAddressFieldsValueService {

    /**
     * Save a physicalAddressFieldsValue.
     *
     * @param physicalAddressFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    PhysicalAddressFieldsValueDTO save(PhysicalAddressFieldsValueDTO physicalAddressFieldsValueDTO);

    /**
     * Get all the physicalAddressFieldsValues.
     *
     * @return the list of entities.
     */
    List<PhysicalAddressFieldsValueDTO> findAll();


    /**
     * Get the "id" physicalAddressFieldsValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PhysicalAddressFieldsValueDTO> findOne(UUID id);

    /**
     * Delete the "id" physicalAddressFieldsValue.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
