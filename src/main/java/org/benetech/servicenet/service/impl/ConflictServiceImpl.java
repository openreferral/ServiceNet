package org.benetech.servicenet.service.impl;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.enumeration.ConflictStateEnum;
import org.benetech.servicenet.repository.ConflictRepository;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.dto.OwnerDTO;
import org.benetech.servicenet.service.mapper.ConflictMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Conflict.
 */
@Service
@Transactional
public class ConflictServiceImpl implements ConflictService {

    private final Logger log = LoggerFactory.getLogger(ConflictServiceImpl.class);

    private final ConflictRepository conflictRepository;

    private final ConflictMapper conflictMapper;

    private final UserService userService;

    private final OrganizationRepository organizationRepository;

    public ConflictServiceImpl(ConflictRepository conflictRepository, ConflictMapper conflictMapper,
        UserService userService, OrganizationRepository organizationRepository) {
        this.conflictRepository = conflictRepository;
        this.conflictMapper = conflictMapper;
        this.userService = userService;
        this.organizationRepository = organizationRepository;
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
        return conflictRepository.findAll().stream()
            .map(conflictMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the conflicts on page.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ConflictDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Conflicts");
        return conflictRepository.findAll(pageable)
            .map(conflictMapper::toDto);
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
        return conflictRepository.findById(id)
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

    /**
     * Get all the Conflict with resourceId and ownerId.
     *
     * @param resourceId the id of the resource entity
     * @param ownerId the id of the owner entity
     */
    @Override
    public List<ConflictDTO> findAllWithResourceIdAndOwnerId(UUID resourceId, UUID ownerId) {
        log.debug("Request to get all Conflicts with resourceId: {}, and ownerId: {}", resourceId, ownerId);
        return conflictRepository.findAllWithResourceIdAndOwnerId(resourceId, ownerId).stream().map(conflictMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the Conflict with resourceId.
     *
     * @param resourceId the id of the resource entity
     */
    @Override
    public List<ConflictDTO> findAllPendingWithResourceId(UUID resourceId) {
        log.debug("Request to get all Conflicts with resourceId: {}.", resourceId);
        return conflictRepository.findAllPendingWithResourceId(resourceId).stream()
            .map(conflictMapper::toDto)
            .peek(conflictDTO -> {
                Organization organization = organizationRepository.getOne(conflictDTO.getPartnerResourceId());
                OwnerDTO owner = userService.getUserDtoOfOrganization(organization);
                conflictDTO.setOwner(owner);
            })
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get max offeredValueDate of Conflict with resourceId.
     *
     * @param resourceId the id of the resource entity
     */
    @Override
    public Optional<ZonedDateTime> findMostRecentOfferedValueDate(UUID resourceId) {
        log.debug("Request to get conflict's  most recent stateDate with conflict's resourceId: {}.", resourceId);
        return conflictRepository.findMostRecentOfferedValueDate(resourceId);
    }

    @Override
    public List<Conflict> findAllPendingWithResourceIdAndPartnerResourceId(UUID resourceId, UUID partnerResourceId) {
        log.debug("Request to get all Conflicts with resourceId: {}.", resourceId);
        return conflictRepository.findByResourceIdAndPartnerResourceIdAndState(
            resourceId, partnerResourceId, ConflictStateEnum.PENDING);
    }
}
