package org.benetech.servicenet.service.impl;

import java.util.UUID;
import org.benetech.servicenet.service.OrganizationFieldsValueService;
import org.benetech.servicenet.domain.OrganizationFieldsValue;
import org.benetech.servicenet.repository.OrganizationFieldsValueRepository;
import org.benetech.servicenet.service.dto.OrganizationFieldsValueDTO;
import org.benetech.servicenet.service.mapper.OrganizationFieldsValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link OrganizationFieldsValue}.
 */
@Service
@Transactional
public class OrganizationFieldsValueServiceImpl implements OrganizationFieldsValueService {

    private final Logger log = LoggerFactory.getLogger(OrganizationFieldsValueServiceImpl.class);

    private final OrganizationFieldsValueRepository organizationFieldsValueRepository;

    private final OrganizationFieldsValueMapper organizationFieldsValueMapper;

    public OrganizationFieldsValueServiceImpl(
        OrganizationFieldsValueRepository organizationFieldsValueRepository,
        OrganizationFieldsValueMapper organizationFieldsValueMapper
    ) {
        this.organizationFieldsValueRepository = organizationFieldsValueRepository;
        this.organizationFieldsValueMapper = organizationFieldsValueMapper;
    }

    /**
     * Save a organizationFieldsValue.
     *
     * @param organizationFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public OrganizationFieldsValueDTO save(OrganizationFieldsValueDTO organizationFieldsValueDTO) {
        log.debug("Request to save OrganizationFieldsValue : {}", organizationFieldsValueDTO);
        OrganizationFieldsValue organizationFieldsValue = organizationFieldsValueMapper.toEntity(organizationFieldsValueDTO);
        organizationFieldsValue = organizationFieldsValueRepository.save(organizationFieldsValue);
        return organizationFieldsValueMapper.toDto(organizationFieldsValue);
    }

    /**
     * Get all the organizationFieldsValues.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrganizationFieldsValueDTO> findAll() {
        log.debug("Request to get all OrganizationFieldsValues");
        return organizationFieldsValueRepository.findAll().stream()
            .map(organizationFieldsValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one organizationFieldsValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationFieldsValueDTO> findOne(UUID id) {
        log.debug("Request to get OrganizationFieldsValue : {}", id);
        return organizationFieldsValueRepository.findById(id)
            .map(organizationFieldsValueMapper::toDto);
    }

    /**
     * Delete the organizationFieldsValue by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete OrganizationFieldsValue : {}", id);
        organizationFieldsValueRepository.deleteById(id);
    }
}
