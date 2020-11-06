package org.benetech.servicenet.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.benetech.servicenet.domain.Beneficiary;
import org.benetech.servicenet.service.dto.OrganizationOptionDTO;
import org.benetech.servicenet.service.dto.ReferralDTO;

import org.benetech.servicenet.service.dto.ReferralMadeFromUserDTO;
import org.benetech.servicenet.service.dto.ReferralMadeToUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.Referral}.
 */
public interface ReferralService {

    /**
     * Save a referral.
     *
     * @param referralDTO the entity to save.
     * @return the persisted entity.
     */
    ReferralDTO save(ReferralDTO referralDTO);

    /**
     * Get all the referrals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReferralDTO> findAll(Pageable pageable);

    /**
     * Get the "id" referral.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReferralDTO> findOne(UUID id);

    /**
     * Delete the "id" referral.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    void checkIn(Beneficiary beneficiary, boolean isBeneficiaryNew, UUID cboId, UUID locationId);

    void refer(Beneficiary beneficiary, UUID cboId, UUID fromLocId, Map<UUID, UUID> organizationLocs);

    /**
     * Search for curent user's referrals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReferralDTO> findCurrentUsersReferrals(ZonedDateTime since, String status, Pageable pageable);

    Page<ReferralMadeFromUserDTO> getNumberOfReferralsMadeFromUser(UUID to, Pageable pageable);

    Page<ReferralMadeToUserDTO> getReferralsMadeToUser(String status, Pageable pageable);

    List<OrganizationOptionDTO> getOrganizationOptionsForCurrentUser();
}
