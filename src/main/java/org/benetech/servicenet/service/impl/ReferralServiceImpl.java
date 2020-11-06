package org.benetech.servicenet.service.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.benetech.servicenet.domain.Beneficiary;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.repository.LocationRepository;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.service.MessageService;
import org.benetech.servicenet.service.ReferralService;
import org.benetech.servicenet.domain.Referral;
import org.benetech.servicenet.repository.ReferralRepository;
import org.benetech.servicenet.service.SmsService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.OrganizationOptionDTO;
import org.benetech.servicenet.service.dto.ReferralDTO;
import org.benetech.servicenet.service.dto.ReferralMadeFromUserDTO;
import org.benetech.servicenet.service.dto.ReferralMadeToUserDTO;
import org.benetech.servicenet.service.dto.SmsMessage;
import org.benetech.servicenet.service.mapper.ReferralMapper;
import org.benetech.servicenet.util.IdentifierUtils;
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

    private final OrganizationRepository organizationRepository;

    private final UserService userService;

    private final MessageService messageService;

    private final SmsService smsService;

    private final LocationRepository locationRepository;

    public ReferralServiceImpl(ReferralRepository referralRepository,
        ReferralMapper referralMapper,
        OrganizationRepository organizationRepository,
        UserService userService,
        MessageService messageService,
        SmsService smsService,
        LocationRepository locationRepository
    ) {
        this.referralRepository = referralRepository;
        this.referralMapper = referralMapper;
        this.organizationRepository = organizationRepository;
        this.userService = userService;
        this.messageService = messageService;
        this.smsService = smsService;
        this.locationRepository = locationRepository;
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
    public void checkIn(Beneficiary beneficiary, boolean isBeneficiaryNew, UUID cboId, UUID locationId) {
        Organization cbo = organizationRepository.getOne(cboId);
        List<Referral> referrals = referralRepository
            .findAllByBeneficiaryIdAndReferredTo(beneficiary.getId(), cboId, locationId);
        ZonedDateTime now = ZonedDateTime.now();
        if (referrals.isEmpty()) {
            Referral referral = new Referral();
            referral.setBeneficiary(beneficiary);
            referral.setFrom(cbo);
            Location location = (locationId != null) ? locationRepository.getOne(locationId)
                : cbo.getLocations().stream().findFirst().orElse(null);
            referral.setFromLocation(location);
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
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setTo(beneficiary.getPhoneNumber());
        if (isBeneficiaryNew) {
            smsMessage.setMessage(messageService.get("sms.checkin.new",
                cbo.getName(), IdentifierUtils.toBase36(beneficiary.getIdentifier())));
        } else {
            smsMessage.setMessage(messageService.get("sms.checkin.existing",
                cbo.getName(), IdentifierUtils.toBase36(beneficiary.getIdentifier())));
        }
        smsService.send(smsMessage);
    }

    @Override
    public void refer(Beneficiary beneficiary, UUID cboId, UUID fromLocId, Map<UUID, UUID> organizationLocs) {
        ZonedDateTime now = ZonedDateTime.now();
        List<String> orgNames = new ArrayList<>();
        Organization cbo = organizationRepository.getOne(cboId);
        organizationLocs.forEach((UUID orgId, UUID locId) -> {
            Organization organization = organizationRepository.getOne(orgId);
            orgNames.add(organization.getName());
            Referral referral = new Referral();
            referral.setBeneficiary(beneficiary);
            referral.setFrom(cbo);
            Location fromLocation = (fromLocId != null) ? locationRepository.getOne(fromLocId)
                : cbo.getLocations().stream().findFirst().orElse(null);
            referral.setFromLocation(fromLocation);
            Location toLocation = (locId != null) ? locationRepository.getOne(locId)
                : organization.getLocations().stream().findFirst().orElse(null);
            referral.setToLocation(toLocation);
            referral.setTo(organization);
            referral.sentAt(now);

            referralRepository.save(referral);
        });
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setTo(beneficiary.getPhoneNumber());
        smsMessage.setMessage(messageService.get("sms.referral.sent",
            String.join(", ", orgNames), IdentifierUtils.toBase36(beneficiary.getIdentifier())));
        smsService.send(smsMessage);
    }

    @Override
    public Page<ReferralDTO> findCurrentUsersReferrals(ZonedDateTime since, String status, Pageable pageable) {
        UserProfile currentUser = userService.getCurrentUserProfile();
        return referralRepository.findByUserProfileSince(currentUser,
            since,
            Objects.equals(status, Referral.SENT),
            Objects.equals(status, Referral.FULFILLED), pageable)
            .map(referralMapper::toDto);
    }

    @Override
    public Page<ReferralMadeFromUserDTO> getNumberOfReferralsMadeFromUser(UUID to, Pageable pageable) {
        UserProfile currentUser = userService.getCurrentUserProfile();
        if (to == null) {
            return referralRepository.getNumberOfReferralsMadeFromUser(currentUser, pageable);
        } else {
            return referralRepository.getNumberOfReferralsMadeFromUser(currentUser, to, pageable);
        }
    }

    @Override
    public Page<ReferralMadeToUserDTO> getReferralsMadeToUser(String status, Pageable pageable) {
        UserProfile currentUser = userService.getCurrentUserProfile();
        return referralRepository.getReferralsMadeToUser(
            currentUser,
            Objects.equals(status, Referral.SENT),
            Objects.equals(status, Referral.FULFILLED),
            pageable
        );
    }

    @Override
    public List<OrganizationOptionDTO> getOrganizationOptionsForCurrentUser() {
        UserProfile currentUser = userService.getCurrentUserProfile();
        return referralRepository.getMadeToOptionsForCurrentUser(currentUser);
    }
}
