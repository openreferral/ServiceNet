package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.EligibilityDTO;

import java.util.List;
import java.util.Optional;

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
     * Get all the eligibilities.
     *
     * @return the list of entities
     */
    List<EligibilityDTO> findAll();


    /**
     * Get the "id" eligibility.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EligibilityDTO> findOne(Long id);

    /**
     * Delete the "id" eligibility.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
