package org.benetech.servicenet.service.impl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.benetech.servicenet.domain.Beneficiary;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.repository.BeneficiaryRepository;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.service.ReferralService;
import org.benetech.servicenet.domain.Referral;
import org.benetech.servicenet.repository.ReferralRepository;
import org.benetech.servicenet.service.dto.ReferralDTO;
import org.benetech.servicenet.service.mapper.ReferralMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Referral}.
 */
@Service
@Transactional
public class ReferralServiceImpl implements ReferralService {

    private final Logger log = LoggerFactory.getLogger(ReferralServiceImpl.class);

    private final ReferralRepository referralRepository;

    private final ReferralMapper referralMapper;

    private final BeneficiaryRepository beneficiaryRepository;

    private final OrganizationRepository organizationRepository;

    public ReferralServiceImpl(ReferralRepository referralRepository,
        ReferralMapper referralMapper,
        BeneficiaryRepository beneficiaryRepository,
        OrganizationRepository organizationRepository
    ) {
        this.referralRepository = referralRepository;
        this.referralMapper = referralMapper;
        this.beneficiaryRepository = beneficiaryRepository;
        this.organizationRepository = organizationRepository;
    }

    /**
     * Save a referral.
     *
     * @param referralDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ReferralDTO save(ReferralDTO referralDTO) {
        log.debug("Request to save Referral : {}", referralDTO);
        Referral referral = referralMapper.toEntity(referralDTO);
        referral = referralRepository.save(referral);
        return referralMapper.toDto(referral);
    }

    /**
     * Get all the referrals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReferralDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Referrals");
        return referralRepository.findAll(pageable)
            .map(referralMapper::toDto);
    }

    /**
     * Get one referral by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReferralDTO> findOne(UUID id) {
        log.debug("Request to get Referral : {}", id);
        return referralRepository.findById(id)
            .map(referralMapper::toDto);
    }

    /**
     * Delete the referral by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Referral : {}", id);
        referralRepository.deleteById(id);
    }

    @Override
    public void checkIn(UUID beneficiaryId, UUID cboId) {
        List<Referral> referrals = referralRepository
            .findAllByBeneficiaryIdAndReferredTo(beneficiaryId, cboId);
        ZonedDateTime now = ZonedDateTime.now();
        if (referrals.isEmpty()) {
            Beneficiary beneficiary = beneficiaryRepository.getOne(beneficiaryId);
            Organization cbo = organizationRepository.getOne(cboId);

            Referral referral = new Referral();
            referral.setBeneficiary(beneficiary);
            referral.setFrom(cbo);
            referral.setTo(cbo);
            referral.sentAt(now);
            referral.setFulfilledAt(now);

            referralRepository.save(referral);
        } else {
            for (Referral referral : referrals) {
                referral.fulfilledAt(now);
                referralRepository.save(referral);
            }
        }
    }

    @Override
    public void refer(Beneficiary beneficiary, Organization cbo, List<UUID> organizationIds) {
        ZonedDateTime now = ZonedDateTime.now();
        for (UUID orgId : organizationIds) {
            Organization organization = organizationRepository.getOne(orgId);
            Referral referral = new Referral();
            referral.setBeneficiary(beneficiary);
            referral.setFrom(cbo);
            referral.setTo(organization);
            referral.sentAt(now);

            referralRepository.save(referral);
        }
    }
}
