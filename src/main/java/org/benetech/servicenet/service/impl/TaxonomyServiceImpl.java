package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.repository.TaxonomyRepository;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.dto.TaxonomyDTO;
import org.benetech.servicenet.service.mapper.TaxonomyMapper;
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
 * Service Implementation for managing Taxonomy.
 */
@Service
@Transactional
public class TaxonomyServiceImpl implements TaxonomyService {

    private final Logger log = LoggerFactory.getLogger(TaxonomyServiceImpl.class);

    private final TaxonomyRepository taxonomyRepository;

    private final TaxonomyMapper taxonomyMapper;

    public TaxonomyServiceImpl(TaxonomyRepository taxonomyRepository, TaxonomyMapper taxonomyMapper) {
        this.taxonomyRepository = taxonomyRepository;
        this.taxonomyMapper = taxonomyMapper;
    }

    /**
     * Save a taxonomy.
     *
     * @param taxonomyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TaxonomyDTO save(TaxonomyDTO taxonomyDTO) {
        log.debug("Request to save Taxonomy : {}", taxonomyDTO);

        Taxonomy taxonomy = taxonomyMapper.toEntity(taxonomyDTO);
        taxonomy = taxonomyRepository.save(taxonomy);
        return taxonomyMapper.toDto(taxonomy);
    }

    /**
     * Get all the taxonomies.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaxonomyDTO> findAll() {
        log.debug("Request to get all Taxonomies");
        return taxonomyRepository.findAll().stream()
            .map(taxonomyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one taxonomy by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TaxonomyDTO> findOne(UUID id) {
        log.debug("Request to get Taxonomy : {}", id);
        return taxonomyRepository.findById(id)
            .map(taxonomyMapper::toDto);
    }

    /**
     * Delete the taxonomy by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Taxonomy : {}", id);
        taxonomyRepository.deleteById(id);
    }
}
