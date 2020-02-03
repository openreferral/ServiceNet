package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.service.dto.PostalAddressFieldsValueDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.PostalAddressFieldsValue}.
 */
public interface PostalAddressFieldsValueService {

    /**
     * Save a postalAddressFieldsValue.
     *
     * @param postalAddressFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    PostalAddressFieldsValueDTO save(PostalAddressFieldsValueDTO postalAddressFieldsValueDTO);

    /**
     * Get all the postalAddressFieldsValues.
     *
     * @return the list of entities.
     */
    List<PostalAddressFieldsValueDTO> findAll();


    /**
     * Get the "id" postalAddressFieldsValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostalAddressFieldsValueDTO> findOne(UUID id);

    /**
     * Delete the "id" postalAddressFieldsValue.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
