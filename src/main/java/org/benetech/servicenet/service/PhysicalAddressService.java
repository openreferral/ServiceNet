package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.service.dto.AddressDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PhysicalAddress.
 */
public interface PhysicalAddressService {

    /**
     * Save a physicalAddress.
     *
     * @param physicalAddressDTO the entity to save
     * @return the persisted entity
     */
    AddressDTO save(AddressDTO physicalAddressDTO);

    /**
     * Save a physicalAddress.
     *
     * @param physicalAddress the entity to save
     * @return the persisted entity
     */
    PhysicalAddress save(PhysicalAddress physicalAddress);

    /**
     * Get all the physicalAddresses.
     *
     * @return the list of entities
     */
    List<AddressDTO> findAll();

    /**
     * Get all the physicalAddresses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AddressDTO> findAll(Pageable pageable);

    /**
     * Get the "id" physicalAddress.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AddressDTO> findOne(UUID id);

    /**
     * Delete the "id" physicalAddress.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
