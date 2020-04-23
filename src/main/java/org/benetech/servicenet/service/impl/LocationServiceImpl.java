package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.repository.LocationRepository;
import org.benetech.servicenet.service.GeocodingResultService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.mapper.LocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Location.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    private final GeocodingResultService geocodingResultService;

    @Value("${similarity-ratio.credentials.google-api}")
    private String googleApiKey;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper,
        GeocodingResultService geocodingResultService) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.geocodingResultService = geocodingResultService;
    }

    /**
     * Save a location.
     *
     * @param locationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LocationDTO save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);

        return locationMapper.toDto(save(locationMapper.toEntity(locationDTO)));
    }

    @Override
    public Location save(Location location) {
        location.setGeocodingResults(
            geocodingResultService.findAllForAddressOrFetchIfEmpty(
                location.getPhysicalAddress()));
        return locationRepository.save(location);
    }

    /**
     * Get all the locations.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocationDTO> findAll() {
        log.debug("Request to get all Locations");
        return locationRepository.findAll().stream()
            .map(locationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the locations on page
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAll(pageable)
            .map(locationMapper::toDto);
    }

    /**
     * get all the locations where PhysicalAddress is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<LocationDTO> findAllWherePhysicalAddressIsNull() {
        log.debug("Request to get all locations where PhysicalAddress is null");
        return StreamSupport
            .stream(locationRepository.findAll().spliterator(), false)
            .filter(location -> location.getPhysicalAddress() == null)
            .map(locationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * get all the locations where PhysicalAddress is null on page
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LocationDTO> findAllWherePhysicalAddressIsNull(Pageable pageable) {
        log.debug("Request to get all locations where PhysicalAddress is null");
        return locationRepository.findAll(pageable)
            .map(locationMapper::toDto);
    }

    /**
     * get all the locations where PostalAddress is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<LocationDTO> findAllWherePostalAddressIsNull() {
        log.debug("Request to get all locations where PostalAddress is null");
        return StreamSupport
            .stream(locationRepository.findAll().spliterator(), false)
            .filter(location -> location.getPostalAddress() == null)
            .map(locationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * get all the locations where PostalAddress is null.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LocationDTO> findAllWherePostalAddressIsNull(Pageable pageable) {
        log.debug("Request to get all locations where PostalAddress is null");
        return locationRepository.findAll(pageable)
            .map(locationMapper::toDto);
    }

    /**
     * get all the locations where RegularSchedule is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<LocationDTO> findAllWhereRegularScheduleIsNull() {
        log.debug("Request to get all locations where RegularSchedule is null");
        return StreamSupport
            .stream(locationRepository.findAll().spliterator(), false)
            .filter(location -> location.getRegularSchedule() == null)
            .map(locationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * get all the locations where RegularSchedule is null.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LocationDTO> findAllWhereRegularScheduleIsNull(Pageable pageable) {
        log.debug("Request to get all locations where RegularSchedule is null");
        return locationRepository.findAll(pageable)
            .map(locationMapper::toDto);
    }

    @Override
    public Optional<Location> findWithEagerAssociations(String externalDbId, String providerName) {
        return locationRepository.findOneWithEagerAssociationsByExternalDbIdAndProviderName(externalDbId, providerName);
    }

    /**
     * Get one location by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LocationDTO> findOne(UUID id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id)
            .map(locationMapper::toDto);
    }

    /**
     * Delete the location by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }
}
