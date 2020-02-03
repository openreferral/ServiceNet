package org.benetech.servicenet.service.impl;

import java.util.UUID;
import org.benetech.servicenet.service.PhysicalAddressFieldsValueService;
import org.benetech.servicenet.domain.PhysicalAddressFieldsValue;
import org.benetech.servicenet.repository.PhysicalAddressFieldsValueRepository;
import org.benetech.servicenet.service.dto.PhysicalAddressFieldsValueDTO;
import org.benetech.servicenet.service.mapper.PhysicalAddressFieldsValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PhysicalAddressFieldsValue}.
 */
@Service
@Transactional
public class PhysicalAddressFieldsValueServiceImpl implements PhysicalAddressFieldsValueService {

    private final Logger log = LoggerFactory.getLogger(PhysicalAddressFieldsValueServiceImpl.class);

    private final PhysicalAddressFieldsValueRepository physicalAddressFieldsValueRepository;

    private final PhysicalAddressFieldsValueMapper physicalAddressFieldsValueMapper;

    public PhysicalAddressFieldsValueServiceImpl(
        PhysicalAddressFieldsValueRepository physicalAddressFieldsValueRepository,
        PhysicalAddressFieldsValueMapper physicalAddressFieldsValueMapper
    ) {
        this.physicalAddressFieldsValueRepository = physicalAddressFieldsValueRepository;
        this.physicalAddressFieldsValueMapper = physicalAddressFieldsValueMapper;
    }

    /**
     * Save a physicalAddressFieldsValue.
     *
     * @param physicalAddressFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PhysicalAddressFieldsValueDTO save(PhysicalAddressFieldsValueDTO physicalAddressFieldsValueDTO) {
        log.debug("Request to save PhysicalAddressFieldsValue : {}", physicalAddressFieldsValueDTO);
        PhysicalAddressFieldsValue physicalAddressFieldsValue = physicalAddressFieldsValueMapper
            .toEntity(physicalAddressFieldsValueDTO);
        physicalAddressFieldsValue = physicalAddressFieldsValueRepository.save(physicalAddressFieldsValue);
        return physicalAddressFieldsValueMapper.toDto(physicalAddressFieldsValue);
    }

    /**
     * Get all the physicalAddressFieldsValues.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PhysicalAddressFieldsValueDTO> findAll() {
        log.debug("Request to get all PhysicalAddressFieldsValues");
        return physicalAddressFieldsValueRepository.findAll().stream()
            .map(physicalAddressFieldsValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one physicalAddressFieldsValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PhysicalAddressFieldsValueDTO> findOne(UUID id) {
        log.debug("Request to get PhysicalAddressFieldsValue : {}", id);
        return physicalAddressFieldsValueRepository.findById(id)
            .map(physicalAddressFieldsValueMapper::toDto);
    }

    /**
     * Delete the physicalAddressFieldsValue by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PhysicalAddressFieldsValue : {}", id);
        physicalAddressFieldsValueRepository.deleteById(id);
    }
}
