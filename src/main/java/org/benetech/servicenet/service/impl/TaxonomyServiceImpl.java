package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.repository.TaxonomyRepository;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.dto.TaxonomyDTO;
import org.benetech.servicenet.service.mapper.TaxonomyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        taxonomy = save(taxonomy);
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
     * Get all the taxonomies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaxonomyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Taxonomies");
        return taxonomyRepository.findAll(pageable)
            .map(taxonomyMapper::toDto);
    }

    /**
     * Get provider's taxonomies.
     *
     * @param providerName provider's name
     * @return the list of entities
     */
    @Override
    public List<TaxonomyDTO> findByProvider(String providerName) {
        return taxonomyRepository.findAllByProviderName(providerName).stream()
            .map(taxonomyMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get the associated taxonomies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaxonomyDTO> findAssociatedTaxonomies(Pageable pageable) {
        log.debug("Request to get associated Taxonomies");
        return taxonomyRepository.findAssociatedTaxonomies(pageable)
            .map(taxonomyMapper::toDto);
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

    @Override
    public Optional<Taxonomy> findForExternalDb(String externalDbId, String providerName) {
        return taxonomyRepository.findOneByExternalDbIdAndProviderName(externalDbId, providerName);
    }

    @Override
    public Optional<Taxonomy> findForTaxonomyId(String taxonomyId, String providerName) {
        return taxonomyRepository.findOneByTaxonomyIdAndProviderName(taxonomyId, providerName);
    }

    /**
     * Get one taxonomy by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Taxonomy> findById(UUID id) {
        log.debug("Request to get Taxonomy : {}", id);
        return taxonomyRepository.findById(id);
    }

    @Override
    public Taxonomy save(Taxonomy taxonomy) {
        return taxonomyRepository.save(taxonomy);
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
