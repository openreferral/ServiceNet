package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.PhoneDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Phone.
 */
public interface PhoneService {

    /**
     * Save a phone.
     *
     * @param phoneDTO the entity to save
     * @return the persisted entity
     */
    PhoneDTO save(PhoneDTO phoneDTO);

    /**
     * Get all the phones.
     *
     * @return the list of entities
     */
    List<PhoneDTO> findAll();

    /**
     * Get all the phones.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PhoneDTO> findAll(Pageable pageable);

    /**
     * Get the "id" phone.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PhoneDTO> findOne(UUID id);

    /**
     * Delete the "id" phone.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
