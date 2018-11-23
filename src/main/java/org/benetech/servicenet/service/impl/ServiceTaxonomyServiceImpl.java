package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.repository.ServiceTaxonomyRepository;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.dto.ServiceTaxonomyDTO;
import org.benetech.servicenet.service.mapper.ServiceTaxonomyMapper;
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
 * Service Implementation for managing ServiceTaxonomy.
 */
@Service
@Transactional
public class ServiceTaxonomyServiceImpl implements ServiceTaxonomyService {

    private final Logger log = LoggerFactory.getLogger(ServiceTaxonomyServiceImpl.class);

    private final ServiceTaxonomyRepository serviceTaxonomyRepository;

    private final ServiceTaxonomyMapper serviceTaxonomyMapper;

    public ServiceTaxonomyServiceImpl(ServiceTaxonomyRepository serviceTaxonomyRepository,
                                      ServiceTaxonomyMapper serviceTaxonomyMapper) {
        this.serviceTaxonomyRepository = serviceTaxonomyRepository;
        this.serviceTaxonomyMapper = serviceTaxonomyMapper;
    }

    /**
     * Save a serviceTaxonomy.
     *
     * @param serviceTaxonomyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ServiceTaxonomyDTO save(ServiceTaxonomyDTO serviceTaxonomyDTO) {
        log.debug("Request to save ServiceTaxonomy : {}", serviceTaxonomyDTO);

        ServiceTaxonomy serviceTaxonomy = serviceTaxonomyMapper.toEntity(serviceTaxonomyDTO);
        serviceTaxonomy = serviceTaxonomyRepository.save(serviceTaxonomy);
        return serviceTaxonomyMapper.toDto(serviceTaxonomy);
    }

    /**
     * Get all the serviceTaxonomies.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServiceTaxonomyDTO> findAll() {
        log.debug("Request to get all ServiceTaxonomies");
        return serviceTaxonomyRepository.findAll().stream()
            .map(serviceTaxonomyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one serviceTaxonomy by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceTaxonomyDTO> findOne(UUID id) {
        log.debug("Request to get ServiceTaxonomy : {}", id);
        return serviceTaxonomyRepository.findById(id)
            .map(serviceTaxonomyMapper::toDto);
    }

    /**
     * Delete the serviceTaxonomy by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ServiceTaxonomy : {}", id);
        serviceTaxonomyRepository.deleteById(id);
    }
}
