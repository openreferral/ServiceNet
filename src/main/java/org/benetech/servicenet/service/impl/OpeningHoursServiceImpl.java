package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.repository.OpeningHoursRepository;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.benetech.servicenet.service.mapper.OpeningHoursMapper;
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
 * Service Implementation for managing OpeningHours.
 */
@Service
@Transactional
public class OpeningHoursServiceImpl implements OpeningHoursService {

    private final Logger log = LoggerFactory.getLogger(OpeningHoursServiceImpl.class);

    private final OpeningHoursRepository openingHoursRepository;

    private final OpeningHoursMapper openingHoursMapper;

    public OpeningHoursServiceImpl(OpeningHoursRepository openingHoursRepository, OpeningHoursMapper openingHoursMapper) {
        this.openingHoursRepository = openingHoursRepository;
        this.openingHoursMapper = openingHoursMapper;
    }

    /**
     * Save a openingHours.
     *
     * @param openingHoursDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OpeningHoursDTO save(OpeningHoursDTO openingHoursDTO) {
        log.debug("Request to save OpeningHours : {}", openingHoursDTO);

        OpeningHours openingHours = openingHoursMapper.toEntity(openingHoursDTO);
        openingHours = openingHoursRepository.save(openingHours);
        return openingHoursMapper.toDto(openingHours);
    }

    /**
     * Get all the openingHours.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<OpeningHoursDTO> findAll() {
        log.debug("Request to get all OpeningHours");
        return openingHoursRepository.findAll().stream()
            .map(openingHoursMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one openingHours by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OpeningHoursDTO> findOne(UUID id) {
        log.debug("Request to get OpeningHours : {}", id);
        return openingHoursRepository.findById(id)
            .map(openingHoursMapper::toDto);
    }

    /**
     * Delete the openingHours by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete OpeningHours : {}", id);
        openingHoursRepository.deleteById(id);
    }
}
