package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.repository.AccessibilityForDisabilitiesRepository;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;
import org.benetech.servicenet.service.mapper.AccessibilityForDisabilitiesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing AccessibilityForDisabilities.
 */
@Service
@Transactional
public class AccessibilityForDisabilitiesServiceImpl implements AccessibilityForDisabilitiesService {

    private final Logger log = LoggerFactory.getLogger(AccessibilityForDisabilitiesServiceImpl.class);

    private final AccessibilityForDisabilitiesRepository accessibilityForDisabilitiesRepository;

    private final AccessibilityForDisabilitiesMapper accessibilityForDisabilitiesMapper;

    public AccessibilityForDisabilitiesServiceImpl(
        AccessibilityForDisabilitiesRepository accessibilityForDisabilitiesRepository,
        AccessibilityForDisabilitiesMapper accessibilityForDisabilitiesMapper) {
        this.accessibilityForDisabilitiesRepository = accessibilityForDisabilitiesRepository;
        this.accessibilityForDisabilitiesMapper = accessibilityForDisabilitiesMapper;
    }

    /**
     * Save a accessibilityForDisabilities.
     *
     * @param accessibilityForDisabilitiesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AccessibilityForDisabilitiesDTO save(AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO) {
        log.debug("Request to save AccessibilityForDisabilities : {}", accessibilityForDisabilitiesDTO);

        AccessibilityForDisabilities accessibilityForDisabilities =
            accessibilityForDisabilitiesMapper.toEntity(accessibilityForDisabilitiesDTO);
        accessibilityForDisabilities = accessibilityForDisabilitiesRepository.save(accessibilityForDisabilities);
        return accessibilityForDisabilitiesMapper.toDto(accessibilityForDisabilities);
    }

    /**
     * Get all the accessibilityForDisabilities.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AccessibilityForDisabilitiesDTO> findAll() {
        log.debug("Request to get all AccessibilityForDisabilities");
        return accessibilityForDisabilitiesRepository.findAll().stream()
            .map(accessibilityForDisabilitiesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one accessibilityForDisabilities by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AccessibilityForDisabilitiesDTO> findOne(Long id) {
        log.debug("Request to get AccessibilityForDisabilities : {}", id);
        return accessibilityForDisabilitiesRepository.findById(id)
            .map(accessibilityForDisabilitiesMapper::toDto);
    }

    /**
     * Delete the accessibilityForDisabilities by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccessibilityForDisabilities : {}", id);
        accessibilityForDisabilitiesRepository.deleteById(id);
    }
}
