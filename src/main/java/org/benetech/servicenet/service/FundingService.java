package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.FundingDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Funding.
 */
public interface FundingService {

    /**
     * Save a funding.
     *
     * @param fundingDTO the entity to save
     * @return the persisted entity
     */
    FundingDTO save(FundingDTO fundingDTO);

    /**
     * Get all the fundings.
     *
     * @return the list of entities
     */
    List<FundingDTO> findAll();


    /**
     * Get the "id" funding.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FundingDTO> findOne(Long id);

    /**
     * Delete the "id" funding.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
