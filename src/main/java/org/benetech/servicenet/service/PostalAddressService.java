package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.service.dto.AddressDTO;
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
    AddressDTO save(AddressDTO postalAddressDTO);

    /**
     * Save a postalAddress.
     *
     * @param postalAddress the entity to save
     * @return the persisted entity
     */
    PostalAddress save(PostalAddress postalAddress);

    /**
     * Get all the postalAddresses.
     *
     * @return the list of entities
     */
    List<AddressDTO> findAll();

    /**
     * Get all the postalAddresses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AddressDTO> findAll(Pageable pageable);

    /**
     * Get the "id" postalAddress.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AddressDTO> findOne(UUID id);

    /**
     * Delete the "id" postalAddress.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
