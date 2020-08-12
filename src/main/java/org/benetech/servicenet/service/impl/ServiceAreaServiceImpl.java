package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.ServiceArea;
import org.benetech.servicenet.repository.ServiceAreaRepository;
import org.benetech.servicenet.service.ServiceAreaService;
import org.benetech.servicenet.service.dto.ServiceAreaDTO;
import org.benetech.servicenet.service.mapper.ServiceAreaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing ServiceArea.
 */
@Service
@Transactional
public class ServiceAreaServiceImpl implements ServiceAreaService {

    private final Logger log = LoggerFactory.getLogger(ServiceAreaServiceImpl.class);

    private final ServiceAreaRepository serviceAreaRepository;

    private final ServiceAreaMapper serviceAreaMapper;

    public ServiceAreaServiceImpl(ServiceAreaRepository serviceAreaRepository, ServiceAreaMapper serviceAreaMapper) {
        this.serviceAreaRepository = serviceAreaRepository;
        this.serviceAreaMapper = serviceAreaMapper;
    }

    /**
     * Save a serviceArea.
     *
     * @param serviceAreaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ServiceAreaDTO save(ServiceAreaDTO serviceAreaDTO) {
        log.debug("Request to save ServiceArea : {}", serviceAreaDTO);

        ServiceArea serviceArea = serviceAreaMapper.toEntity(serviceAreaDTO);
        serviceArea = serviceAreaRepository.save(serviceArea);
        return serviceAreaMapper.toDto(serviceArea);
    }

    /**
     * Get all the serviceAreas.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServiceAreaDTO> findAll() {
        log.debug("Request to get all ServiceAreas");
        return serviceAreaRepository.findAll().stream()
            .map(serviceAreaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the serviceAreas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceAreaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceAreas");
        return serviceAreaRepository.findAll(pageable)
            .map(serviceAreaMapper::toDto);
    }

    /**
     * Get one serviceArea by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceAreaDTO> findOne(UUID id) {
        log.debug("Request to get ServiceArea : {}", id);
        return serviceAreaRepository.findById(id)
            .map(serviceAreaMapper::toDto);
    }

    /**
     * Delete the serviceArea by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ServiceArea : {}", id);
        serviceAreaRepository.deleteById(id);
    }
}
