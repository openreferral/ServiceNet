package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.ServiceFieldsValueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.ServiceFieldsValue}.
 */
public interface ServiceFieldsValueService {

    /**
     * Save a serviceFieldsValue.
     *
     * @param serviceFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    ServiceFieldsValueDTO save(ServiceFieldsValueDTO serviceFieldsValueDTO);

    /**
     * Get all the serviceFieldsValues.
     *
     * @return the list of entities.
     */
    List<ServiceFieldsValueDTO> findAll();

    /**
     * Get all the serviceFieldsValues.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    Page<ServiceFieldsValueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" serviceFieldsValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceFieldsValueDTO> findOne(UUID id);

    /**
     * Delete the "id" serviceFieldsValue.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
