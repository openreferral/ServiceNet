package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.repository.OrganizationMatchRepository;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.benetech.servicenet.service.mapper.OrganizationMatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing OrganizationMatch.
 */
@Service
@Transactional
public class OrganizationMatchServiceImpl implements OrganizationMatchService {

    private final Logger log = LoggerFactory.getLogger(OrganizationMatchServiceImpl.class);

    private final OrganizationMatchRepository organizationMatchRepository;

    private final OrganizationMatchMapper organizationMatchMapper;

    public OrganizationMatchServiceImpl(OrganizationMatchRepository organizationMatchRepository,
                                        OrganizationMatchMapper organizationMatchMapper) {
        this.organizationMatchRepository = organizationMatchRepository;
        this.organizationMatchMapper = organizationMatchMapper;
    }

    /**
     * Save a organizationMatch.
     *
     * @param organizationMatchDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrganizationMatchDTO save(OrganizationMatchDTO organizationMatchDTO) {
        log.debug("Request to save OrganizationMatch : {}", organizationMatchDTO);

        OrganizationMatch organizationMatch = organizationMatchMapper.toEntity(organizationMatchDTO);
        organizationMatch = organizationMatchRepository.save(organizationMatch);
        return organizationMatchMapper.toDto(organizationMatch);
    }

    /**
     * Get all the organizationMatches.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrganizationMatchDTO> findAll() {
        log.debug("Request to get all OrganizationMatches");
        return organizationMatchRepository.findAll().stream()
            .map(organizationMatchMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one organizationMatch by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationMatchDTO> findOne(Long id) {
        log.debug("Request to get OrganizationMatch : {}", id);
        return organizationMatchRepository.findById(id)
            .map(organizationMatchMapper::toDto);
    }

    /**
     * Delete the organizationMatch by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrganizationMatch : {}", id);
        organizationMatchRepository.deleteById(id);
    }
}
