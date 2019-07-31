package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.repository.PostalAddressRepository;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.benetech.servicenet.service.mapper.PostalAddressMapper;
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
 * Service Implementation for managing PostalAddress.
 */
@Service
@Transactional
public class PostalAddressServiceImpl implements PostalAddressService {

    private final Logger log = LoggerFactory.getLogger(PostalAddressServiceImpl.class);

    private final PostalAddressRepository postalAddressRepository;

    private final PostalAddressMapper postalAddressMapper;

    public PostalAddressServiceImpl(PostalAddressRepository postalAddressRepository,
                                    PostalAddressMapper postalAddressMapper) {
        this.postalAddressRepository = postalAddressRepository;
        this.postalAddressMapper = postalAddressMapper;
    }

    /**
     * Save a postalAddress.
     *
     * @param postalAddressDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PostalAddressDTO save(PostalAddressDTO postalAddressDTO) {
        log.debug("Request to save PostalAddress : {}", postalAddressDTO);

        PostalAddress postalAddress = postalAddressMapper.toEntity(postalAddressDTO);
        postalAddress = postalAddressRepository.save(postalAddress);
        return postalAddressMapper.toDto(postalAddress);
    }

    /**
     * Get all the postalAddresses.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PostalAddressDTO> findAll() {
        log.debug("Request to get all PostalAddresses");
        return postalAddressRepository.findAll().stream()
            .map(postalAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the postalAddresses on page
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostalAddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostalAddresses");
        return postalAddressRepository.findAll(pageable)
            .map(postalAddressMapper::toDto);
    }

    /**
     * Get one postalAddress by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PostalAddressDTO> findOne(UUID id) {
        log.debug("Request to get PostalAddress : {}", id);
        return postalAddressRepository.findById(id)
            .map(postalAddressMapper::toDto);
    }

    /**
     * Delete the postalAddress by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PostalAddress : {}", id);
        postalAddressRepository.deleteById(id);
    }
}
