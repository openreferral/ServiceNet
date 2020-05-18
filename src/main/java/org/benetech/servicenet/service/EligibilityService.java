package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Eligibility.
 */
public interface EligibilityService {

    /**
     * Save a eligibility.
     *
     * @param eligibilityDTO the entity to save
     * @return the persisted entity
     */
    EligibilityDTO save(EligibilityDTO eligibilityDTO);

    /**
     * Save a eligibility.
     *
     * @param eligibility the entity to save
     * @return the persisted entity
     */
    Eligibility save(Eligibility eligibility);

    /**
     * Get all the eligibilities.
     *
     * @return the list of entities
     */
    List<EligibilityDTO> findAll();

    /**
     * Get all the eligibilities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EligibilityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eligibility.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EligibilityDTO> findOne(UUID id);

    /**
     * Delete the "id" eligibility.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
