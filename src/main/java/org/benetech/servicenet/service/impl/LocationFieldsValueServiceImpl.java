package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.LocationFieldsValue;
import org.benetech.servicenet.repository.LocationFieldsValueRepository;
import org.benetech.servicenet.service.LocationFieldsValueService;
import org.benetech.servicenet.service.dto.LocationFieldsValueDTO;
import org.benetech.servicenet.service.mapper.LocationFieldsValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocationFieldsValue}.
 */
@Service
@Transactional
public class LocationFieldsValueServiceImpl implements LocationFieldsValueService {

    private final Logger log = LoggerFactory.getLogger(LocationFieldsValueServiceImpl.class);

    private final LocationFieldsValueRepository locationFieldsValueRepository;

    private final LocationFieldsValueMapper locationFieldsValueMapper;

    public LocationFieldsValueServiceImpl(
        LocationFieldsValueRepository locationFieldsValueRepository,
        LocationFieldsValueMapper locationFieldsValueMapper
    ) {
        this.locationFieldsValueRepository = locationFieldsValueRepository;
        this.locationFieldsValueMapper = locationFieldsValueMapper;
    }

    /**
     * Save a locationFieldsValue.
     *
     * @param locationFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LocationFieldsValueDTO save(LocationFieldsValueDTO locationFieldsValueDTO) {
        log.debug("Request to save LocationFieldsValue : {}", locationFieldsValueDTO);
        LocationFieldsValue locationFieldsValue = locationFieldsValueMapper.toEntity(locationFieldsValueDTO);
        locationFieldsValue = locationFieldsValueRepository.save(locationFieldsValue);
        return locationFieldsValueMapper.toDto(locationFieldsValue);
    }

    /**
     * Get all the locationFieldsValues.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocationFieldsValueDTO> findAll() {
        log.debug("Request to get all LocationFieldsValues");
        return locationFieldsValueRepository.findAll().stream()
            .map(locationFieldsValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the locationFieldsValues.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LocationFieldsValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LocationFieldsValues");
        return locationFieldsValueRepository.findAll(pageable)
            .map(locationFieldsValueMapper::toDto);
    }

    /**
     * Get one locationFieldsValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LocationFieldsValueDTO> findOne(UUID id) {
        log.debug("Request to get LocationFieldsValue : {}", id);
        return locationFieldsValueRepository.findById(id)
            .map(locationFieldsValueMapper::toDto);
    }

    /**
     * Delete the locationFieldsValue by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete LocationFieldsValue : {}", id);
        locationFieldsValueRepository.deleteById(id);
    }
}
