package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.PostalAddressFieldsValueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the postalAddressFieldsValues.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    Page<PostalAddressFieldsValueDTO> findAll(Pageable pageable);

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
