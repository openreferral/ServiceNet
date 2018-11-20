package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.repository.HolidayScheduleRepository;
import org.benetech.servicenet.service.HolidayScheduleService;
import org.benetech.servicenet.service.dto.HolidayScheduleDTO;
import org.benetech.servicenet.service.mapper.HolidayScheduleMapper;
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
 * Service Implementation for managing HolidaySchedule.
 */
@Service
@Transactional
public class HolidayScheduleServiceImpl implements HolidayScheduleService {

    private final Logger log = LoggerFactory.getLogger(HolidayScheduleServiceImpl.class);

    private final HolidayScheduleRepository holidayScheduleRepository;

    private final HolidayScheduleMapper holidayScheduleMapper;

    public HolidayScheduleServiceImpl(HolidayScheduleRepository holidayScheduleRepository,
                                      HolidayScheduleMapper holidayScheduleMapper) {
        this.holidayScheduleRepository = holidayScheduleRepository;
        this.holidayScheduleMapper = holidayScheduleMapper;
    }

    /**
     * Save a holidaySchedule.
     *
     * @param holidayScheduleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public HolidayScheduleDTO save(HolidayScheduleDTO holidayScheduleDTO) {
        log.debug("Request to save HolidaySchedule : {}", holidayScheduleDTO);

        HolidaySchedule holidaySchedule = holidayScheduleMapper.toEntity(holidayScheduleDTO);
        holidaySchedule = holidayScheduleRepository.save(holidaySchedule);
        return holidayScheduleMapper.toDto(holidaySchedule);
    }

    /**
     * Get all the holidaySchedules.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<HolidayScheduleDTO> findAll() {
        log.debug("Request to get all HolidaySchedules");
        return holidayScheduleRepository.findAll().stream()
            .map(holidayScheduleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one holidaySchedule by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HolidayScheduleDTO> findOne(UUID id) {
        log.debug("Request to get HolidaySchedule : {}", id);
        return holidayScheduleRepository.findById(id)
            .map(holidayScheduleMapper::toDto);
    }

    /**
     * Delete the holidaySchedule by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete HolidaySchedule : {}", id);
        holidayScheduleRepository.deleteById(id);
    }
}
