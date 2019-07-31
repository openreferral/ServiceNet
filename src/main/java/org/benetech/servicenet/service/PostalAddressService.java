package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PostalAddress.
 */
public interface PostalAddressService {

    /**
     * Save a postalAddress.
     *
     * @param postalAddressDTO the entity to save
     * @return the persisted entity
     */
    PostalAddressDTO save(PostalAddressDTO postalAddressDTO);

    /**
     * Get all the postalAddresses.
     *
     * @return the list of entities
     */
    List<PostalAddressDTO> findAll();

    /**
     * Get all the postalAddresses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PostalAddressDTO> findAll(Pageable pageable);

    /**
     * Get the "id" postalAddress.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PostalAddressDTO> findOne(UUID id);

    /**
     * Delete the "id" postalAddress.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
