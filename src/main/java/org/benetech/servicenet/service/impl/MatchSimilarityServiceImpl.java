package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.service.MatchSimilarityService;
import org.benetech.servicenet.domain.MatchSimilarity;
import org.benetech.servicenet.repository.MatchSimilarityRepository;
import org.benetech.servicenet.service.dto.MatchSimilarityDTO;
import org.benetech.servicenet.service.mapper.MatchSimilarityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MatchSimilarity}.
 */
@Service
@Transactional
public class MatchSimilarityServiceImpl implements MatchSimilarityService {

    private final Logger log = LoggerFactory.getLogger(MatchSimilarityServiceImpl.class);

    private final MatchSimilarityRepository matchSimilarityRepository;

    private final MatchSimilarityMapper matchSimilarityMapper;

    public MatchSimilarityServiceImpl(MatchSimilarityRepository matchSimilarityRepository,
                                      MatchSimilarityMapper matchSimilarityMapper) {
        this.matchSimilarityRepository = matchSimilarityRepository;
        this.matchSimilarityMapper = matchSimilarityMapper;
    }

    /**
     * Save a matchSimilarity.
     *
     * @param matchSimilarityDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public MatchSimilarityDTO save(MatchSimilarityDTO matchSimilarityDTO) {
        log.debug("Request to save MatchSimilarity : {}", matchSimilarityDTO);
        MatchSimilarity matchSimilarity = matchSimilarityMapper.toEntity(matchSimilarityDTO);
        matchSimilarity = matchSimilarityRepository.save(matchSimilarity);
        return matchSimilarityMapper.toDto(matchSimilarity);
    }

    /**
     * Get all the matchSimilarities.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<MatchSimilarityDTO> findAll() {
        log.debug("Request to get all MatchSimilarities");
        return matchSimilarityRepository.findAll().stream()
            .map(matchSimilarityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one matchSimilarity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MatchSimilarityDTO> findOne(UUID id) {
        log.debug("Request to get MatchSimilarity : {}", id);
        return matchSimilarityRepository.findById(id)
            .map(matchSimilarityMapper::toDto);
    }

    /**
     * Delete the matchSimilarity by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete MatchSimilarity : {}", id);
        matchSimilarityRepository.deleteById(id);
    }
}
