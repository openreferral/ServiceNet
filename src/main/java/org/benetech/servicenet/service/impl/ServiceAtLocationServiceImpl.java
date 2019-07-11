package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.repository.ServiceAtLocationRepository;
import org.benetech.servicenet.service.ServiceAtLocationService;
import org.benetech.servicenet.service.dto.ServiceAtLocationDTO;
import org.benetech.servicenet.service.mapper.ServiceAtLocationMapper;
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
 * Service Implementation for managing ServiceAtLocation.
 */
@Service
@Transactional
public class ServiceAtLocationServiceImpl implements ServiceAtLocationService {

    private final Logger log = LoggerFactory.getLogger(ServiceAtLocationServiceImpl.class);

    private final ServiceAtLocationRepository serviceAtLocationRepository;

    private final ServiceAtLocationMapper serviceAtLocationMapper;

    public ServiceAtLocationServiceImpl(ServiceAtLocationRepository serviceAtLocationRepository,
                                        ServiceAtLocationMapper serviceAtLocationMapper) {
        this.serviceAtLocationRepository = serviceAtLocationRepository;
        this.serviceAtLocationMapper = serviceAtLocationMapper;
    }

    /**
     * Save a serviceAtLocation.
     *
     * @param serviceAtLocationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ServiceAtLocationDTO save(ServiceAtLocationDTO serviceAtLocationDTO) {
        log.debug("Request to save ServiceAtLocation : {}", serviceAtLocationDTO);

        ServiceAtLocation serviceAtLocation = serviceAtLocationMapper.toEntity(serviceAtLocationDTO);
        serviceAtLocation = serviceAtLocationRepository.save(serviceAtLocation);
        return serviceAtLocationMapper.toDto(serviceAtLocation);
    }

    /**
     * Get all the serviceAtLocations.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServiceAtLocationDTO> findAll() {
        log.debug("Request to get all ServiceAtLocations");
        return serviceAtLocationRepository.findAll().stream()
            .map(serviceAtLocationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one serviceAtLocation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceAtLocationDTO> findOne(UUID id) {
        log.debug("Request to get ServiceAtLocation : {}", id);
        return serviceAtLocationRepository.findById(id)
            .map(serviceAtLocationMapper::toDto);
    }

    @Override
    public Optional<ServiceAtLocation> findForExternalDb(String externalDbId, String providerName) {
        return serviceAtLocationRepository.findOneByExternalDbIdAndProviderName(externalDbId, providerName);
    }

    /**
     * Delete the serviceAtLocation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ServiceAtLocation : {}", id);
        serviceAtLocationRepository.deleteById(id);
    }
}
