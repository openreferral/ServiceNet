package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.service.dto.ContactDetailsFieldsValueDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.ContactDetailsFieldsValue}.
 */
public interface ContactDetailsFieldsValueService {

    /**
     * Save a contactDetailsFieldsValue.
     *
     * @param contactDetailsFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    ContactDetailsFieldsValueDTO save(ContactDetailsFieldsValueDTO contactDetailsFieldsValueDTO);

    /**
     * Get all the contactDetailsFieldsValues.
     *
     * @return the list of entities.
     */
    List<ContactDetailsFieldsValueDTO> findAll();


    /**
     * Get the "id" contactDetailsFieldsValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactDetailsFieldsValueDTO> findOne(UUID id);

    /**
     * Delete the "id" contactDetailsFieldsValue.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
