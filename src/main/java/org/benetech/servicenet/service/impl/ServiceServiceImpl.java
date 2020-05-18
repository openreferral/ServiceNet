package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.repository.ServiceRepository;
import org.benetech.servicenet.service.ServiceAtLocationService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.benetech.servicenet.service.mapper.ServiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final ServiceAtLocationService serviceAtLocationService;

    private final ServiceTaxonomyService serviceTaxonomyService;

    public ServiceServiceImpl(ServiceRepository serviceRepository, ServiceMapper serviceMapper,
        ServiceAtLocationService serviceAtLocationService, ServiceTaxonomyService serviceTaxonomyService) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
        this.serviceAtLocationService = serviceAtLocationService;
        this.serviceTaxonomyService = serviceTaxonomyService;
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
     * Save a service.
     *
     * @param service the entity to save
     * @return the persisted entity
     */
    @Override
    public Service save(Service service) {
        log.debug("Request to save Service : {}", service);

        return serviceRepository.save(service);
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
     * Get all the services on page.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Services");
        return serviceRepository.findAll(pageable)
            .map(serviceMapper::toDto);
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

    @Override
    public Optional<Service> findWithEagerAssociations(String externalDbId, String providerName) {
        return serviceRepository.findOneWithEagerAssociationsByExternalDbIdAndProviderName(externalDbId, providerName);
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
     * Get one service by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Service> findById(UUID id) {
        log.debug("Request to get Service : {}", id);
        return serviceRepository.findById(id);
    }

    /**
     * Delete the service by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Service : {}", id);
        Optional<Service> service = findById(id);
        if (service.isPresent()) {
            for (ServiceTaxonomy st : service.get().getTaxonomies()) {
                serviceTaxonomyService.delete(st.getId());
            }
            for (ServiceAtLocation sat : service.get().getLocations()) {
                serviceAtLocationService.delete(sat.getId());
            }
            serviceRepository.deleteById(id);
        }
    }
}
