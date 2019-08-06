package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.FundingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
     * Get all the fundings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FundingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" funding.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FundingDTO> findOne(UUID id);

    /**
     * Delete the "id" funding.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
