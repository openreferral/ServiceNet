package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.repository.FundingRepository;
import org.benetech.servicenet.service.FundingService;
import org.benetech.servicenet.service.dto.FundingDTO;
import org.benetech.servicenet.service.mapper.FundingMapper;
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
 * Service Implementation for managing Funding.
 */
@Service
@Transactional
public class FundingServiceImpl implements FundingService {

    private final Logger log = LoggerFactory.getLogger(FundingServiceImpl.class);

    private final FundingRepository fundingRepository;

    private final FundingMapper fundingMapper;

    public FundingServiceImpl(FundingRepository fundingRepository, FundingMapper fundingMapper) {
        this.fundingRepository = fundingRepository;
        this.fundingMapper = fundingMapper;
    }

    /**
     * Save a funding.
     *
     * @param fundingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FundingDTO save(FundingDTO fundingDTO) {
        log.debug("Request to save Funding : {}", fundingDTO);

        Funding funding = fundingMapper.toEntity(fundingDTO);
        funding = fundingRepository.save(funding);
        return fundingMapper.toDto(funding);
    }

    /**
     * Get all the fundings.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FundingDTO> findAll() {
        log.debug("Request to get all Fundings");
        return fundingRepository.findAll().stream()
            .map(fundingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the fundings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FundingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fundings");
        return fundingRepository.findAll(pageable)
            .map(fundingMapper::toDto);
    }

    /**
     * Get one funding by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FundingDTO> findOne(UUID id) {
        log.debug("Request to get Funding : {}", id);
        return fundingRepository.findById(id)
            .map(fundingMapper::toDto);
    }

    /**
     * Delete the funding by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Funding : {}", id);
        fundingRepository.deleteById(id);
    }
}
