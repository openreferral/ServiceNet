package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.LocationExclusion;
import org.benetech.servicenet.repository.LocationExclusionRepository;
import org.benetech.servicenet.service.LocationExclusionService;
import org.benetech.servicenet.service.dto.LocationExclusionDTO;
import org.benetech.servicenet.service.mapper.LocationExclusionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocationExclusion}.
 */
@Service
@Transactional
public class LocationExclusionServiceImpl implements LocationExclusionService {

    private final Logger log = LoggerFactory.getLogger(LocationExclusionServiceImpl.class);

    private final LocationExclusionRepository locationExclusionRepository;

    private final LocationExclusionMapper locationExclusionMapper;

    public LocationExclusionServiceImpl(LocationExclusionRepository locationExclusionRepository,
        LocationExclusionMapper locationExclusionMapper) {
        this.locationExclusionRepository = locationExclusionRepository;
        this.locationExclusionMapper = locationExclusionMapper;
    }

    /**
     * Save a locationExclusion.
     *
     * @param locationExclusionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LocationExclusionDTO save(LocationExclusionDTO locationExclusionDTO) {
        log.debug("Request to save LocationExclusion : {}", locationExclusionDTO);
        LocationExclusion locationExclusion = locationExclusionMapper.toEntity(locationExclusionDTO);
        locationExclusion = locationExclusionRepository.save(locationExclusion);
        return locationExclusionMapper.toDto(locationExclusion);
    }

    /**
     * Get all the locationExclusions.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocationExclusionDTO> findAll() {
        log.debug("Request to get all LocationExclusions");
        return locationExclusionRepository.findAll().stream()
            .map(locationExclusionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the locationExclusions.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LocationExclusionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LocationExclusions");
        return locationExclusionRepository.findAll(pageable)
            .map(locationExclusionMapper::toDto);
    }

    /**
     * Get one locationExclusion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LocationExclusionDTO> findOne(UUID id) {
        log.debug("Request to get LocationExclusion : {}", id);
        return locationExclusionRepository.findById(id)
            .map(locationExclusionMapper::toDto);
    }

    /**
     * Delete the locationExclusion by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete LocationExclusion : {}", id);
        locationExclusionRepository.deleteById(id);
    }
}
