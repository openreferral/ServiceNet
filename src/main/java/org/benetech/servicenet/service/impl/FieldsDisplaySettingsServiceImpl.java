package org.benetech.servicenet.service.impl;

import java.util.UUID;
import org.benetech.servicenet.service.FieldsDisplaySettingsService;
import org.benetech.servicenet.domain.FieldsDisplaySettings;
import org.benetech.servicenet.repository.FieldsDisplaySettingsRepository;
import org.benetech.servicenet.service.dto.FieldsDisplaySettingsDTO;
import org.benetech.servicenet.service.mapper.FieldsDisplaySettingsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link FieldsDisplaySettings}.
 */
@Service
@Transactional
public class FieldsDisplaySettingsServiceImpl implements FieldsDisplaySettingsService {

    private final Logger log = LoggerFactory.getLogger(FieldsDisplaySettingsServiceImpl.class);

    private final FieldsDisplaySettingsRepository fieldsDisplaySettingsRepository;

    private final FieldsDisplaySettingsMapper fieldsDisplaySettingsMapper;

    public FieldsDisplaySettingsServiceImpl(
        FieldsDisplaySettingsRepository fieldsDisplaySettingsRepository,
        FieldsDisplaySettingsMapper fieldsDisplaySettingsMapper
    ) {
        this.fieldsDisplaySettingsRepository = fieldsDisplaySettingsRepository;
        this.fieldsDisplaySettingsMapper = fieldsDisplaySettingsMapper;
    }

    /**
     * Save a fieldsDisplaySettings.
     *
     * @param fieldsDisplaySettingsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FieldsDisplaySettingsDTO save(FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO) {
        log.debug("Request to save FieldsDisplaySettings : {}", fieldsDisplaySettingsDTO);
        FieldsDisplaySettings fieldsDisplaySettings = fieldsDisplaySettingsMapper.toEntity(fieldsDisplaySettingsDTO);
        fieldsDisplaySettings = fieldsDisplaySettingsRepository.save(fieldsDisplaySettings);
        return fieldsDisplaySettingsMapper.toDto(fieldsDisplaySettings);
    }

    /**
     * Get all the fieldsDisplaySettings.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FieldsDisplaySettingsDTO> findAll() {
        log.debug("Request to get all FieldsDisplaySettings");
        return fieldsDisplaySettingsRepository.findAll().stream()
            .map(fieldsDisplaySettingsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one fieldsDisplaySettings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FieldsDisplaySettingsDTO> findOne(UUID id) {
        log.debug("Request to get FieldsDisplaySettings : {}", id);
        return fieldsDisplaySettingsRepository.findById(id)
            .map(fieldsDisplaySettingsMapper::toDto);
    }

    /**
     * Delete the fieldsDisplaySettings by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete FieldsDisplaySettings : {}", id);
        fieldsDisplaySettingsRepository.deleteById(id);
    }
}
