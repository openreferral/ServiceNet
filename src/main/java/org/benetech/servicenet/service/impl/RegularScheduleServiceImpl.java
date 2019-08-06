package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.repository.RegularScheduleRepository;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.dto.RegularScheduleDTO;
import org.benetech.servicenet.service.mapper.RegularScheduleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing RegularSchedule.
 */
@Service
@Transactional
public class RegularScheduleServiceImpl implements RegularScheduleService {

    private final Logger log = LoggerFactory.getLogger(RegularScheduleServiceImpl.class);

    private final RegularScheduleRepository regularScheduleRepository;

    private final RegularScheduleMapper regularScheduleMapper;

    public RegularScheduleServiceImpl(RegularScheduleRepository regularScheduleRepository,
                                      RegularScheduleMapper regularScheduleMapper) {
        this.regularScheduleRepository = regularScheduleRepository;
        this.regularScheduleMapper = regularScheduleMapper;
    }

    /**
     * Save a regularSchedule.
     *
     * @param regularScheduleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RegularScheduleDTO save(RegularScheduleDTO regularScheduleDTO) {
        log.debug("Request to save RegularSchedule : {}", regularScheduleDTO);

        RegularSchedule regularSchedule = regularScheduleMapper.toEntity(regularScheduleDTO);
        regularSchedule = regularScheduleRepository.save(regularSchedule);
        return regularScheduleMapper.toDto(regularSchedule);
    }

    /**
     * Get all the regularSchedules.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RegularScheduleDTO> findAll() {
        log.debug("Request to get all RegularSchedules");
        return regularScheduleRepository.findAll().stream()
            .map(regularScheduleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the regularSchedules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RegularScheduleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RegularSchedules");
        return regularScheduleRepository.findAll(pageable)
            .map(regularScheduleMapper::toDto);
    }

    /**
     * Get one regularSchedule by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RegularScheduleDTO> findOne(UUID id) {
        log.debug("Request to get RegularSchedule : {}", id);
        return regularScheduleRepository.findById(id)
            .map(regularScheduleMapper::toDto);
    }

    /**
     * Delete the regularSchedule by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete RegularSchedule : {}", id);
        regularScheduleRepository.deleteById(id);
    }
}
