package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.repository.ConflictRepository;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.mapper.ConflictMapper;
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
 * Service Implementation for managing Conflict.
 */
@Service
@Transactional
public class ConflictServiceImpl implements ConflictService {

    private final Logger log = LoggerFactory.getLogger(ConflictServiceImpl.class);

    private final ConflictRepository conflictRepository;

    private final ConflictMapper conflictMapper;

    public ConflictServiceImpl(ConflictRepository conflictRepository, ConflictMapper conflictMapper) {
        this.conflictRepository = conflictRepository;
        this.conflictMapper = conflictMapper;
    }

    /**
     * Save a conflict.
     *
     * @param conflictDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ConflictDTO save(ConflictDTO conflictDTO) {
        log.debug("Request to save Conflict : {}", conflictDTO);

        Conflict conflict = conflictMapper.toEntity(conflictDTO);
        conflict = conflictRepository.save(conflict);
        return conflictMapper.toDto(conflict);
    }

    /**
     * Get all the conflicts.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ConflictDTO> findAll() {
        log.debug("Request to get all Conflicts");
        return conflictRepository.findAllWithEagerRelationships().stream()
            .map(conflictMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the Conflict with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ConflictDTO> findAllWithEagerRelationships(Pageable pageable) {
        return conflictRepository.findAllWithEagerRelationships(pageable).map(conflictMapper::toDto);
    }
    

    /**
     * Get one conflict by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ConflictDTO> findOne(UUID id) {
        log.debug("Request to get Conflict : {}", id);
        return conflictRepository.findOneWithEagerRelationships(id)
            .map(conflictMapper::toDto);
    }

    /**
     * Delete the conflict by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Conflict : {}", id);
        conflictRepository.deleteById(id);
    }
}