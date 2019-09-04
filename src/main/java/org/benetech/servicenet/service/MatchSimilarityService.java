package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.MatchSimilarityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.MatchSimilarity}.
 */
public interface MatchSimilarityService {

    /**
     * Save a matchSimilarity.
     *
     * @param matchSimilarityDTO the entity to save.
     * @return the persisted entity.
     */
    MatchSimilarityDTO saveOrUpdate(MatchSimilarityDTO matchSimilarityDTO);

    /**
     * Get all the matchSimilarities.
     *
     * @return the list of entities.
     */
    List<MatchSimilarityDTO> findAll();

    /**
     * Get all the matchSimilarities.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    Page<MatchSimilarityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" matchSimilarity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MatchSimilarityDTO> findOne(UUID id);

    /**
     * Delete the "id" matchSimilarity.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
