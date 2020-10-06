package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.ServiceTaxonomiesDetailsFieldsValue;
import org.benetech.servicenet.repository.ServiceTaxonomiesDetailsFieldsValueRepository;
import org.benetech.servicenet.service.ServiceTaxonomiesDetailsFieldsValueService;
import org.benetech.servicenet.service.dto.ServiceTaxonomiesDetailsFieldsValueDTO;
import org.benetech.servicenet.service.mapper.ServiceTaxonomiesDetailsFieldsValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ServiceTaxonomiesDetailsFieldsValue}.
 */
@Service
@Transactional
public class ServiceTaxonomiesDetailsFieldsValueServiceImpl implements ServiceTaxonomiesDetailsFieldsValueService {

    private final Logger log = LoggerFactory.getLogger(ServiceTaxonomiesDetailsFieldsValueServiceImpl.class);

    private final ServiceTaxonomiesDetailsFieldsValueRepository serviceTaxonomiesDetailsFieldsValueRepository;

    private final ServiceTaxonomiesDetailsFieldsValueMapper serviceTaxonomiesDetailsFieldsValueMapper;

    public ServiceTaxonomiesDetailsFieldsValueServiceImpl(
        ServiceTaxonomiesDetailsFieldsValueRepository serviceTaxonomiesDetailsFieldsValueRepository,
        ServiceTaxonomiesDetailsFieldsValueMapper serviceTaxonomiesDetailsFieldsValueMapper
    ) {
        this.serviceTaxonomiesDetailsFieldsValueRepository = serviceTaxonomiesDetailsFieldsValueRepository;
        this.serviceTaxonomiesDetailsFieldsValueMapper = serviceTaxonomiesDetailsFieldsValueMapper;
    }

    /**
     * Save a serviceTaxonomiesDetailsFieldsValue.
     *
     * @param serviceTaxonomiesDetailsFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ServiceTaxonomiesDetailsFieldsValueDTO save(
        ServiceTaxonomiesDetailsFieldsValueDTO serviceTaxonomiesDetailsFieldsValueDTO
    ) {
        log.debug("Request to save ServiceTaxonomiesDetailsFieldsValue : {}", serviceTaxonomiesDetailsFieldsValueDTO);
        ServiceTaxonomiesDetailsFieldsValue serviceTaxonomiesDetailsFieldsValue = serviceTaxonomiesDetailsFieldsValueMapper
            .toEntity(serviceTaxonomiesDetailsFieldsValueDTO);
        serviceTaxonomiesDetailsFieldsValue = serviceTaxonomiesDetailsFieldsValueRepository
            .save(serviceTaxonomiesDetailsFieldsValue);
        return serviceTaxonomiesDetailsFieldsValueMapper.toDto(serviceTaxonomiesDetailsFieldsValue);
    }

    /**
     * Get all the serviceTaxonomiesDetailsFieldsValues.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServiceTaxonomiesDetailsFieldsValueDTO> findAll() {
        log.debug("Request to get all ServiceTaxonomiesDetailsFieldsValues");
        return serviceTaxonomiesDetailsFieldsValueRepository.findAll().stream()
            .map(serviceTaxonomiesDetailsFieldsValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the serviceTaxonomiesDetailsFieldsValues.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceTaxonomiesDetailsFieldsValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceTaxonomiesDetailsFieldsValues");
        return serviceTaxonomiesDetailsFieldsValueRepository.findAll(pageable)
            .map(serviceTaxonomiesDetailsFieldsValueMapper::toDto);
    }

    /**
     * Get one serviceTaxonomiesDetailsFieldsValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceTaxonomiesDetailsFieldsValueDTO> findOne(UUID id) {
        log.debug("Request to get ServiceTaxonomiesDetailsFieldsValue : {}", id);
        return serviceTaxonomiesDetailsFieldsValueRepository.findById(id)
            .map(serviceTaxonomiesDetailsFieldsValueMapper::toDto);
    }

    /**
     * Delete the serviceTaxonomiesDetailsFieldsValue by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ServiceTaxonomiesDetailsFieldsValue : {}", id);
        serviceTaxonomiesDetailsFieldsValueRepository.deleteById(id);
    }
}
