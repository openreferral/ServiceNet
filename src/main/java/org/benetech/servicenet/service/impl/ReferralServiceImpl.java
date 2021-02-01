package org.benetech.servicenet.service.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
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
import org.benetech.servicenet.service.dto.CheckInsAndReferralsMadeForRecordDTO;
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

    private static final int NAME_WIDTH = 24;

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
            referral.setFromUser(userService.getCurrentUserProfile());

            referralRepository.save(referral);
        } else {
            for (Referral referral : referrals) {
                referral.fulfilledAt(now);
                referralRepository.save(referral);
            }
        }
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setTo(beneficiary.getPhoneNumber());
        String cboName = StringUtils.abbreviate(cbo.getName(), NAME_WIDTH);
        if (isBeneficiaryNew) {
            smsMessage.setMessage(messageService.get("sms.checkin.new",
                cboName, IdentifierUtils.toBase36(beneficiary.getIdentifier())));
        } else {
            smsMessage.setMessage(messageService.get("sms.checkin.existing",
                cboName, IdentifierUtils.toBase36(beneficiary.getIdentifier())));
        }
        smsService.send(smsMessage);
    }

    @Override
    public void refer(Beneficiary beneficiary, UUID cboId, UUID fromLocId, Map<UUID, UUID> organizationLocs) {
        ZonedDateTime now = ZonedDateTime.now();
        List<String> recommendedReferrals = new ArrayList<>();
        Organization cbo = cboId != null ? organizationRepository.getOne(cboId) : null;
        organizationLocs.forEach((UUID orgId, UUID locId) -> {
            Organization organization = organizationRepository.getOne(orgId);
            Referral referral = new Referral();
            referral.setBeneficiary(beneficiary);
            referral.setFrom(cbo);
            if (cbo != null || fromLocId != null) {
                Location fromLocation = (fromLocId != null) ? locationRepository.getOne(fromLocId)
                    : cbo.getLocations().stream().findFirst().orElse(null);
                referral.setFromLocation(fromLocation);
            }
            Location toLocation = (locId != null) ? locationRepository.getOne(locId)
                : organization.getLocations().stream().findFirst().orElse(null);
            referral.setToLocation(toLocation);
            referral.setTo(organization);
            referral.setFromUser(userService.getCurrentUserProfile());
            referral.sentAt(now);

            referralRepository.save(referral);
            String orgName = StringUtils.abbreviate(organization.getName(), NAME_WIDTH);
            recommendedReferrals.add(toLocation != null ?
                orgName + ", " + StringUtils.abbreviate(toLocation.getName(), NAME_WIDTH)
                : orgName);
        });
        SmsMessage smsMessage = new SmsMessage();
        SmsMessage secondSmsMessage = new SmsMessage();
        smsMessage.setTo(beneficiary.getPhoneNumber());
        secondSmsMessage.setTo(beneficiary.getPhoneNumber());
        smsMessage.setMessage(messageService.get("sms.referral.sent",
            String.join("\n", recommendedReferrals)));
        secondSmsMessage.setMessage(messageService.get("sms.referral.sent2",
            IdentifierUtils.toBase36(beneficiary.getIdentifier())));
        smsService.send(smsMessage);
        smsService.send(secondSmsMessage);
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

    @Override
    public Integer getCheckInsToRecordCount(Organization record) {
        List<Referral> checkIns = referralRepository.findAllToAndFrom(record);
        return checkIns.size();
    }

    @Override
    public Integer getReferralsToRecordCount(Organization record) {
        List<Referral> referrals =  referralRepository.findAllToAndNotFrom(record);
        return referrals.size();
    }

    @Override
    public Integer getReferralsFromRecordCount(Organization record) {
        List<Referral> referrals =  referralRepository.findAllFromAndNotTo(record);
        return referrals.size();
    }

    @Override
    public CheckInsAndReferralsMadeForRecordDTO getReferralsMadeForRecord(UUID recordId) {
        Organization record = organizationRepository.getOne(recordId);
        return new CheckInsAndReferralsMadeForRecordDTO(
            recordId,
            getCheckInsToRecordCount(record),
            getReferralsToRecordCount(record),
            getReferralsFromRecordCount(record)
        );
    }

    @Override
    public void removeLocations(Set<Location> locations) {
        List<Referral> referrals = referralRepository.findAllByFromLocations(locations);
        referrals.forEach(
            referral -> referral.setFromLocation(null)
        );
        referralRepository.findAllByToLocations(locations).forEach(
            referral -> {
                referral.setToLocation(null);
                referrals.add(referral);
            }
        );
        referralRepository.saveAll(referrals);
    }
}
