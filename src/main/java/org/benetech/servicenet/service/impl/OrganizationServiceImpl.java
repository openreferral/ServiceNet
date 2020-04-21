package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.mapper.OrganizationMapper;
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
     * Save a organization.
     *
     * @param organization the entity to save
     * @return the persisted entity
     */
    @Override
    public Organization save(Organization organization) {
        log.debug("Request to save Organization : {}", organization);

        return organizationRepository.save(organization);
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
    public List<Organization> findAllWithEagerAssociations() {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAllWithEagerAssociations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAllOthers(String providerName) {
        log.debug("Request to get all Organizations which are not associated with provider: {}", providerName);
        return organizationRepository.findAllByProviderNameNot(providerName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAllOthersExcept(String providerName, List<UUID> exceptIds) {
        log.debug("Request to get all Organizations which are not associated with provider: {} and not in: {}",
            providerName, exceptIds);
        if (exceptIds.size() > 0) {
            return organizationRepository
                .findAllByProviderNameNotAnAndIdNotIn(providerName, exceptIds);
        } else {
            return organizationRepository.findAllByProviderNameNot(providerName);
        }
    }

    /**
     * Get all the organizations on page.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Conflicts");
        return organizationRepository.findAll(pageable)
            .map(organizationMapper::toDto);
    }

    /**
     * get all the organizations where Funding is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findAllWhereFundingIsNull() {
        log.debug("Request to get all organizations where Funding is null");
        return organizationRepository.findAll().stream()
            .filter(organization -> organization.getFunding() == null)
            .map(organizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the organizations on page.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findAllWhereFundingIsNull(Pageable pageable) {
        log.debug("Request to get all organizations where Funding is null");
        return organizationRepository.findAll(pageable)
            //.filter(organization -> organization.getFunding() == null)
            .map(organizationMapper::toDto);
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

    @Override
    @Transactional(readOnly = true)
    public Organization findOneWithEagerAssociations(UUID id) {
        return organizationRepository.findOneWithEagerAssociations(id);
    }

    @Override
    public Optional<Organization> findByIdOrExternalDbId(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return organizationRepository.findByIdOrExternalDbId(uuid, "");
        } catch (IllegalArgumentException e) {
            return organizationRepository.findByIdOrExternalDbId(null, id);
        }
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
}
