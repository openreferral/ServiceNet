package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.mapper.OrganizationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Organization.
 */
@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrganizationDTO save(OrganizationDTO organizationDTO) {
        log.debug("Request to save Organization : {}", organizationDTO);

        Organization organization = organizationMapper.toEntity(organizationDTO);
        organization = organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }

    /**
     * Get all the organizations.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findAllDTOs() {
        log.debug("Request to get all Organizations");
        return findAll().stream()
            .map(organizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAll() {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAllOthers(String providerName) {
        log.debug("Request to get all Organizations which are not associated with provider: {}", providerName);
        return organizationRepository.findAllByProviderNameNot(providerName);
    }

    /**
     * get all the organizations where Funding is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findAllWhereFundingIsNull() {
        log.debug("Request to get all organizations where Funding is null");
        return StreamSupport
            .stream(organizationRepository.findAll().spliterator(), false)
            .filter(organization -> organization.getFunding() == null)
            .map(organizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<Organization> findWithEagerAssociations(String externalDbId, String providerName) {
        return organizationRepository.findOneWithEagerAssociationsByExternalDbIdAndProviderName(externalDbId, providerName);
    }

    /**
     * Get one organization by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationDTO> findOneDTO(UUID id) {
        log.debug("Request to get Organization : {}", id);
        return findOne(id)
            .map(organizationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Organization> findOne(UUID id) {
        return organizationRepository.findById(id);
    }

    /**
     * Delete the organization by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UUID> findAllOrgIdsWithOwnerId(UUID ownerId, Pageable pageable) {
        Page<Object[]> list = organizationRepository.findAllOrgIdsWithOwnerId(ownerId, pageable);
        return new PageImpl<>(getIds(list),
            PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), list.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findAllWithOwnerId(UUID ownerId) {
        return organizationRepository.findAllWithOwnerId(ownerId).stream().map(organizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Organization> findAllWithOwnerId(UUID ownerId, Pageable pageable) {
        return organizationRepository.findAllWithOwnerId(ownerId, pageable);
    }

    private List<UUID> getIds(Page<Object[]> fetchedResult) {
        List<UUID> ids = new LinkedList<>();
        for (Object[] org : fetchedResult) {
            ByteBuffer bb = ByteBuffer.wrap((byte[]) org[0]);
            long high = bb.getLong();
            long low = bb.getLong();
            ids.add(new UUID(high, low));
        }
        return ids;
    }
}
