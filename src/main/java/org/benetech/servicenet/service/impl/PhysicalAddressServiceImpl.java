package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.repository.PhysicalAddressRepository;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.mapper.PhysicalAddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PhysicalAddress.
 */
@Service
@Transactional
public class PhysicalAddressServiceImpl implements PhysicalAddressService {

    private final Logger log = LoggerFactory.getLogger(PhysicalAddressServiceImpl.class);

    private final PhysicalAddressRepository physicalAddressRepository;

    private final PhysicalAddressMapper physicalAddressMapper;

    public PhysicalAddressServiceImpl(PhysicalAddressRepository physicalAddressRepository,
                                      PhysicalAddressMapper physicalAddressMapper) {
        this.physicalAddressRepository = physicalAddressRepository;
        this.physicalAddressMapper = physicalAddressMapper;
    }

    /**
     * Save a physicalAddress.
     *
     * @param physicalAddressDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PhysicalAddressDTO save(PhysicalAddressDTO physicalAddressDTO) {
        log.debug("Request to save PhysicalAddress : {}", physicalAddressDTO);

        PhysicalAddress physicalAddress = physicalAddressMapper.toEntity(physicalAddressDTO);
        physicalAddress = physicalAddressRepository.save(physicalAddress);
        return physicalAddressMapper.toDto(physicalAddress);
    }

    /**
     * Get all the physicalAddresses.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PhysicalAddressDTO> findAll() {
        log.debug("Request to get all PhysicalAddresses");
        return physicalAddressRepository.findAll().stream()
            .map(physicalAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one physicalAddress by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PhysicalAddressDTO> findOne(Long id) {
        log.debug("Request to get PhysicalAddress : {}", id);
        return physicalAddressRepository.findById(id)
            .map(physicalAddressMapper::toDto);
    }

    /**
     * Delete the physicalAddress by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PhysicalAddress : {}", id);
        physicalAddressRepository.deleteById(id);
    }
}
