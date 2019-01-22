package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.ConfidentialRecord;
import org.benetech.servicenet.repository.ConfidentialRecordRepository;
import org.benetech.servicenet.service.ConfidentialRecordService;
import org.benetech.servicenet.service.dto.ConfidentialRecordDTO;
import org.benetech.servicenet.service.mapper.ConfidentialRecordMapper;
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
 * Service Implementation for managing ConfidentialRecord.
 */
@Service
@Transactional
public class ConfidentialRecordServiceImpl implements ConfidentialRecordService {

    private final Logger log = LoggerFactory.getLogger(ConfidentialRecordServiceImpl.class);

    private final ConfidentialRecordRepository confidentialRecordRepository;

    private final ConfidentialRecordMapper confidentialRecordMapper;

    public ConfidentialRecordServiceImpl(ConfidentialRecordRepository confidentialRecordRepository, ConfidentialRecordMapper confidentialRecordMapper) {
        this.confidentialRecordRepository = confidentialRecordRepository;
        this.confidentialRecordMapper = confidentialRecordMapper;
    }

    /**
     * Save a confidentialRecord.
     *
     * @param confidentialRecordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ConfidentialRecordDTO save(ConfidentialRecordDTO confidentialRecordDTO) {
        log.debug("Request to save ConfidentialRecord : {}", confidentialRecordDTO);

        ConfidentialRecord confidentialRecord = confidentialRecordMapper.toEntity(confidentialRecordDTO);
        confidentialRecord = confidentialRecordRepository.save(confidentialRecord);
        return confidentialRecordMapper.toDto(confidentialRecord);
    }

    /**
     * Get all the confidentialRecords.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ConfidentialRecordDTO> findAll() {
        log.debug("Request to get all ConfidentialRecords");
        return confidentialRecordRepository.findAll().stream()
            .map(confidentialRecordMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one confidentialRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConfidentialRecordDTO> findOne(UUID id) {
        log.debug("Request to get ConfidentialRecord : {}", id);
        return confidentialRecordRepository.findById(id)
            .map(confidentialRecordMapper::toDto);
    }

    /**
     * Delete the confidentialRecord by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ConfidentialRecord : {}", id);
        confidentialRecordRepository.deleteById(id);
    }
}
