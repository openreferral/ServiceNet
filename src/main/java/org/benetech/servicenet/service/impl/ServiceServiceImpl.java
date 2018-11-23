package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.repository.ServiceRepository;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.benetech.servicenet.service.mapper.ServiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Service.
 */
@org.springframework.stereotype.Service
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final Logger log = LoggerFactory.getLogger(ServiceServiceImpl.class);

    private final ServiceRepository serviceRepository;

    private final ServiceMapper serviceMapper;

    public ServiceServiceImpl(ServiceRepository serviceRepository, ServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
    }

    /**
     * Save a service.
     *
     * @param serviceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ServiceDTO save(ServiceDTO serviceDTO) {
        log.debug("Request to save Service : {}", serviceDTO);

        Service service = serviceMapper.toEntity(serviceDTO);
        service = serviceRepository.save(service);
        return serviceMapper.toDto(service);
    }

    /**
     * Get all the services.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServiceDTO> findAll() {
        log.debug("Request to get all Services");
        return serviceRepository.findAll().stream()
            .map(serviceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * get all the services where Location is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO> findAllWhereLocationIsNull() {
        log.debug("Request to get all services where Location is null");
        return StreamSupport
            .stream(serviceRepository.findAll().spliterator(), false)
            .filter(service -> service.getLocation() == null)
            .map(serviceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * get all the services where RegularSchedule is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO> findAllWhereRegularScheduleIsNull() {
        log.debug("Request to get all services where RegularSchedule is null");
        return StreamSupport
            .stream(serviceRepository.findAll().spliterator(), false)
            .filter(service -> service.getRegularSchedule() == null)
            .map(serviceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * get all the services where HolidaySchedule is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO> findAllWhereHolidayScheduleIsNull() {
        log.debug("Request to get all services where HolidaySchedule is null");
        return StreamSupport
            .stream(serviceRepository.findAll().spliterator(), false)
            .filter(service -> service.getHolidaySchedule() == null)
            .map(serviceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * get all the services where Funding is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO> findAllWhereFundingIsNull() {
        log.debug("Request to get all services where Funding is null");
        return StreamSupport
            .stream(serviceRepository.findAll().spliterator(), false)
            .filter(service -> service.getFunding() == null)
            .map(serviceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * get all the services where Eligibility is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ServiceDTO> findAllWhereEligibilityIsNull() {
        log.debug("Request to get all services where Eligibility is null");
        return StreamSupport
            .stream(serviceRepository.findAll().spliterator(), false)
            .filter(service -> service.getEligibility() == null)
            .map(serviceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one service by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceDTO> findOne(UUID id) {
        log.debug("Request to get Service : {}", id);
        return serviceRepository.findById(id)
            .map(serviceMapper::toDto);
    }

    /**
     * Delete the service by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Service : {}", id);
        serviceRepository.deleteById(id);
    }
}
