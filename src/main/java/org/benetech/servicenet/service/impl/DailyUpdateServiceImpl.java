package org.benetech.servicenet.service.impl;

import java.util.UUID;
import org.benetech.servicenet.service.DailyUpdateService;
import org.benetech.servicenet.domain.DailyUpdate;
import org.benetech.servicenet.repository.DailyUpdateRepository;
import org.benetech.servicenet.service.dto.DailyUpdateDTO;
import org.benetech.servicenet.service.mapper.DailyUpdateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link DailyUpdate}.
 */
@Service
@Transactional
public class DailyUpdateServiceImpl implements DailyUpdateService {

    private final Logger log = LoggerFactory.getLogger(DailyUpdateServiceImpl.class);

    private final DailyUpdateRepository dailyUpdateRepository;

    private final DailyUpdateMapper dailyUpdateMapper;

    public DailyUpdateServiceImpl(DailyUpdateRepository dailyUpdateRepository, DailyUpdateMapper dailyUpdateMapper) {
        this.dailyUpdateRepository = dailyUpdateRepository;
        this.dailyUpdateMapper = dailyUpdateMapper;
    }

    /**
     * Save a dailyUpdate.
     *
     * @param dailyUpdateDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DailyUpdateDTO save(DailyUpdateDTO dailyUpdateDTO) {
        log.debug("Request to save DailyUpdate : {}", dailyUpdateDTO);
        DailyUpdate dailyUpdate = dailyUpdateMapper.toEntity(dailyUpdateDTO);
        dailyUpdate = dailyUpdateRepository.save(dailyUpdate);
        return dailyUpdateMapper.toDto(dailyUpdate);
    }

    /**
     * Save a dailyUpdate.
     *
     * @param dailyUpdate the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DailyUpdate save(DailyUpdate dailyUpdate) {
        log.debug("Request to save DailyUpdate : {}", dailyUpdate);
        return dailyUpdateRepository.save(dailyUpdate);
    }

    /**
     * Get all the dailyUpdates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DailyUpdateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DailyUpdates");
        return dailyUpdateRepository.findAll(pageable)
            .map(dailyUpdateMapper::toDto);
    }

    /**
     * Get one dailyUpdate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DailyUpdateDTO> findOne(UUID id) {
        log.debug("Request to get DailyUpdate : {}", id);
        return dailyUpdateRepository.findById(id)
            .map(dailyUpdateMapper::toDto);
    }

    /**
     * Delete the dailyUpdate by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete DailyUpdate : {}", id);
        dailyUpdateRepository.deleteById(id);
    }
}
