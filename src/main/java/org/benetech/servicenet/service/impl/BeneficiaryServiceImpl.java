package org.benetech.servicenet.service.impl;

import java.util.UUID;
import org.benetech.servicenet.service.BeneficiaryService;
import org.benetech.servicenet.domain.Beneficiary;
import org.benetech.servicenet.repository.BeneficiaryRepository;
import org.benetech.servicenet.service.dto.BeneficiaryDTO;
import org.benetech.servicenet.service.mapper.BeneficiaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Beneficiary}.
 */
@Service
@Transactional
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final Logger log = LoggerFactory.getLogger(BeneficiaryServiceImpl.class);

    private final BeneficiaryRepository beneficiaryRepository;

    private final BeneficiaryMapper beneficiaryMapper;

    public BeneficiaryServiceImpl(BeneficiaryRepository beneficiaryRepository, BeneficiaryMapper beneficiaryMapper) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.beneficiaryMapper = beneficiaryMapper;
    }

    /**
     * Save a beneficiary.
     *
     * @param beneficiaryDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BeneficiaryDTO save(BeneficiaryDTO beneficiaryDTO) {
        log.debug("Request to save Beneficiary : {}", beneficiaryDTO);
        Beneficiary beneficiary = beneficiaryMapper.toEntity(beneficiaryDTO);
        beneficiary = beneficiaryRepository.save(beneficiary);
        return beneficiaryMapper.toDto(beneficiary);
    }

    /**
     * Get all the beneficiaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Beneficiaries");
        return beneficiaryRepository.findAll(pageable)
            .map(beneficiaryMapper::toDto);
    }

    /**
     * Get one beneficiary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BeneficiaryDTO> findOne(UUID id) {
        log.debug("Request to get Beneficiary : {}", id);
        return beneficiaryRepository.findById(id)
            .map(beneficiaryMapper::toDto);
    }

    /**
     * Get one beneficiary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Beneficiary> getOne(UUID id) {
        log.debug("Request to get Beneficiary : {}", id);
        return beneficiaryRepository.findById(id);
    }

    /**
     * Delete the beneficiary by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Beneficiary : {}", id);
        Beneficiary beneficiary = beneficiaryRepository.getOne(id);
        if (beneficiary.getReferrals() != null) {
            beneficiary.getReferrals().forEach(
                referral -> {
                    referral.setBeneficiary(null);
                }

            );
        }
        beneficiaryRepository.deleteById(id);
    }

    @Override
    public Optional<Beneficiary> findByPhoneNumber(String phoneNumber) {
        return beneficiaryRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Beneficiary create(String phoneNumber) {
        Beneficiary newBeneficiary = new Beneficiary();
        newBeneficiary.setPhoneNumber(phoneNumber);
        newBeneficiary = beneficiaryRepository.save(newBeneficiary);
        return newBeneficiary;
    }
}
