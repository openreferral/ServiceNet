package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.repository.EligibilityRepository;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.mapper.EligibilityMapper;
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
 * Service Implementation for managing Eligibility.
 */
@Service
@Transactional
public class EligibilityServiceImpl implements EligibilityService {

    private final Logger log = LoggerFactory.getLogger(EligibilityServiceImpl.class);

    private final EligibilityRepository eligibilityRepository;

    private final EligibilityMapper eligibilityMapper;

    public EligibilityServiceImpl(EligibilityRepository eligibilityRepository, EligibilityMapper eligibilityMapper) {
        this.eligibilityRepository = eligibilityRepository;
        this.eligibilityMapper = eligibilityMapper;
    }

    /**
     * Save a eligibility.
     *
     * @param eligibilityDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EligibilityDTO save(EligibilityDTO eligibilityDTO) {
        log.debug("Request to save Eligibility : {}", eligibilityDTO);

        Eligibility eligibility = eligibilityMapper.toEntity(eligibilityDTO);
        eligibility = eligibilityRepository.save(eligibility);
        return eligibilityMapper.toDto(eligibility);
    }

    /**
     * Get all the eligibilities.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<EligibilityDTO> findAll() {
        log.debug("Request to get all Eligibilities");
        return eligibilityRepository.findAll().stream()
            .map(eligibilityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one eligibility by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EligibilityDTO> findOne(UUID id) {
        log.debug("Request to get Eligibility : {}", id);
        return eligibilityRepository.findById(id)
            .map(eligibilityMapper::toDto);
    }

    /**
     * Delete the eligibility by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Eligibility : {}", id);
        eligibilityRepository.deleteById(id);
    }
}
