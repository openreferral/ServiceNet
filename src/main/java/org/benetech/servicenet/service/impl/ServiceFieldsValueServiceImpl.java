package org.benetech.servicenet.service.impl;

import java.util.UUID;
import org.benetech.servicenet.service.ServiceFieldsValueService;
import org.benetech.servicenet.domain.ServiceFieldsValue;
import org.benetech.servicenet.repository.ServiceFieldsValueRepository;
import org.benetech.servicenet.service.dto.ServiceFieldsValueDTO;
import org.benetech.servicenet.service.mapper.ServiceFieldsValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ServiceFieldsValue}.
 */
@Service
@Transactional
public class ServiceFieldsValueServiceImpl implements ServiceFieldsValueService {

    private final Logger log = LoggerFactory.getLogger(ServiceFieldsValueServiceImpl.class);

    private final ServiceFieldsValueRepository serviceFieldsValueRepository;

    private final ServiceFieldsValueMapper serviceFieldsValueMapper;

    public ServiceFieldsValueServiceImpl(
        ServiceFieldsValueRepository serviceFieldsValueRepository,
        ServiceFieldsValueMapper serviceFieldsValueMapper
    ) {
        this.serviceFieldsValueRepository = serviceFieldsValueRepository;
        this.serviceFieldsValueMapper = serviceFieldsValueMapper;
    }

    /**
     * Save a serviceFieldsValue.
     *
     * @param serviceFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ServiceFieldsValueDTO save(ServiceFieldsValueDTO serviceFieldsValueDTO) {
        log.debug("Request to save ServiceFieldsValue : {}", serviceFieldsValueDTO);
        ServiceFieldsValue serviceFieldsValue = serviceFieldsValueMapper.toEntity(serviceFieldsValueDTO);
        serviceFieldsValue = serviceFieldsValueRepository.save(serviceFieldsValue);
        return serviceFieldsValueMapper.toDto(serviceFieldsValue);
    }

    /**
     * Get all the serviceFieldsValues.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServiceFieldsValueDTO> findAll() {
        log.debug("Request to get all ServiceFieldsValues");
        return serviceFieldsValueRepository.findAll().stream()
            .map(serviceFieldsValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one serviceFieldsValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceFieldsValueDTO> findOne(UUID id) {
        log.debug("Request to get ServiceFieldsValue : {}", id);
        return serviceFieldsValueRepository.findById(id)
            .map(serviceFieldsValueMapper::toDto);
    }

    /**
     * Delete the serviceFieldsValue by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ServiceFieldsValue : {}", id);
        serviceFieldsValueRepository.deleteById(id);
    }
}
