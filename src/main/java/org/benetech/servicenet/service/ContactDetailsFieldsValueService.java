package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.ContactDetailsFieldsValueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the contactDetailsFieldsValues.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    Page<ContactDetailsFieldsValueDTO> findAll(Pageable pageable);

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
