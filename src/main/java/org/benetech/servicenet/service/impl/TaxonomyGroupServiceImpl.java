package org.benetech.servicenet.service.impl;

import java.util.UUID;
import org.benetech.servicenet.service.TaxonomyGroupService;
import org.benetech.servicenet.domain.TaxonomyGroup;
import org.benetech.servicenet.repository.TaxonomyGroupRepository;
import org.benetech.servicenet.service.dto.TaxonomyGroupDTO;
import org.benetech.servicenet.service.mapper.TaxonomyGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TaxonomyGroup}.
 */
@Service
@Transactional
public class TaxonomyGroupServiceImpl implements TaxonomyGroupService {

    private final Logger log = LoggerFactory.getLogger(TaxonomyGroupServiceImpl.class);

    private final TaxonomyGroupRepository taxonomyGroupRepository;

    private final TaxonomyGroupMapper taxonomyGroupMapper;

    public TaxonomyGroupServiceImpl(TaxonomyGroupRepository taxonomyGroupRepository,
        TaxonomyGroupMapper taxonomyGroupMapper) {
        this.taxonomyGroupRepository = taxonomyGroupRepository;
        this.taxonomyGroupMapper = taxonomyGroupMapper;
    }

    /**
     * Save a taxonomyGroup.
     *
     * @param taxonomyGroupDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TaxonomyGroupDTO save(TaxonomyGroupDTO taxonomyGroupDTO) {
        log.debug("Request to save TaxonomyGroup : {}", taxonomyGroupDTO);
        TaxonomyGroup taxonomyGroup = taxonomyGroupMapper.toEntity(taxonomyGroupDTO);
        taxonomyGroup = taxonomyGroupRepository.save(taxonomyGroup);
        return taxonomyGroupMapper.toDto(taxonomyGroup);
    }

    /**
     * Get all the taxonomyGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaxonomyGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxonomyGroups");
        return taxonomyGroupRepository.findAll(pageable)
            .map(taxonomyGroupMapper::toDto);
    }

    /**
     * Get all the taxonomyGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TaxonomyGroupDTO> findAllWithEagerRelationships(Pageable pageable) {
        return taxonomyGroupRepository.findAllWithEagerRelationships(pageable).map(taxonomyGroupMapper::toDto);
    }


    /**
     * Get one taxonomyGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TaxonomyGroupDTO> findOne(UUID id) {
        log.debug("Request to get TaxonomyGroup : {}", id);
        return taxonomyGroupRepository.findOneWithEagerRelationships(id)
            .map(taxonomyGroupMapper::toDto);
    }

    /**
     * Delete the taxonomyGroup by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete TaxonomyGroup : {}", id);
        taxonomyGroupRepository.deleteById(id);
    }
}
