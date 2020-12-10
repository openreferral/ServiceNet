package org.benetech.servicenet.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.benetech.servicenet.domain.Referral;

import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.service.dto.OrganizationOptionDTO;
import org.benetech.servicenet.service.dto.ReferralMadeFromUserDTO;
import org.benetech.servicenet.service.dto.ReferralMadeToUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Referral entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReferralRepository extends JpaRepository<Referral, UUID> {

    @Query("SELECT referral FROM Referral referral "
        + "JOIN referral.to org "
        + "JOIN org.locations location "
        + "WHERE referral.beneficiary.id = :beneficiaryId AND org.id = :cboId "
        + "AND COALESCE(:locId, NULL) IS NULL OR :locId = location.id "
        + "ORDER BY referral.sentAt ASC")
    List<Referral> findAllByBeneficiaryIdAndReferredTo(@Param("beneficiaryId") UUID beneficiaryId, @Param("cboId") UUID cboId,
        @Param("locId") UUID locId);

    @Query("SELECT referral FROM Referral referral "
        + "LEFT JOIN referral.from org "
        + "LEFT JOIN org.userProfiles userProfile "
        + "WHERE (userProfile = :fromUser OR referral.fromUser = :fromUser) "
        + "AND (COALESCE(:since, NULL) IS NULL OR referral.sentAt >= :since) "
        + "AND (NOT :isSent = true OR (referral.sentAt IS NOT NULL AND referral.fulfilledAt IS NULL))"
        + "AND (NOT :isFulfilled = true OR referral.fulfilledAt IS NOT NULL) "
        + "ORDER BY referral.sentAt ASC")
    Page<Referral> findByUserProfileSince(@Param("fromUser") UserProfile fromUser, @Param("since")
        ZonedDateTime since, @Param("isSent") Boolean isSent, @Param("isFulfilled") Boolean isFulfilled, Pageable pageable);

    @Query("SELECT new org.benetech.servicenet.service.dto.ReferralMadeFromUserDTO("
        + "toOrg.id, toOrg.name, count(toOrg)"
        + ") FROM Referral referral "
        + "JOIN referral.to toOrg "
        + "LEFT JOIN referral.from fromOrg "
        + "LEFT JOIN fromOrg.userProfiles userProfile "
        + "WHERE (userProfile = :currentUser OR referral.fromUser = :currentUser) "
        + "GROUP BY toOrg, referral.sentAt "
        + "ORDER BY referral.sentAt ASC")
    Page<ReferralMadeFromUserDTO> getNumberOfReferralsMadeFromUser(@Param("currentUser") UserProfile currentUser, Pageable pageable);

    @Query("SELECT new org.benetech.servicenet.service.dto.ReferralMadeFromUserDTO("
        + "toOrg.id, toOrg.name, count(toOrg)"
        + ") FROM Referral referral "
        + "JOIN referral.to toOrg "
        + "LEFT JOIN referral.from fromOrg "
        + "LEFT JOIN fromOrg.userProfiles userProfile "
        + "WHERE (userProfile = :currentUser OR referral.fromUser = :currentUser) AND toOrg.id=:to "
        + "GROUP BY toOrg, referral.sentAt "
        + "ORDER BY referral.sentAt ASC")
    Page<ReferralMadeFromUserDTO> getNumberOfReferralsMadeFromUser(@Param("currentUser") UserProfile currentUser, @Param("to") UUID to, Pageable pageable);

    @Query("SELECT new org.benetech.servicenet.service.dto.ReferralMadeToUserDTO("
        + "fromOrg.id, fromOrg.name, referral.fulfilledAt"
        + ") FROM Referral referral "
        + "JOIN referral.to toOrg "
        + "LEFT JOIN referral.from fromOrg "
        + "LEFT JOIN toOrg.userProfiles userProfile "
        + "WHERE (userProfile = :currentUser OR referral.fromUser = :currentUser) "
        + "AND (NOT :isSent = true OR (referral.sentAt IS NOT NULL AND referral.fulfilledAt IS NULL))"
        + "AND (NOT :isFulfilled = true OR referral.fulfilledAt IS NOT NULL) "
        + "ORDER BY referral.sentAt ASC")
    Page<ReferralMadeToUserDTO> getReferralsMadeToUser(@Param("currentUser") UserProfile currentUser, @Param("isSent") Boolean isSent, @Param("isFulfilled") Boolean isFulfilled, Pageable pageable);

    @Query("SELECT new org.benetech.servicenet.service.dto.OrganizationOptionDTO("
        + "toOrg.id, toOrg.name"
        + ") FROM Referral referral "
        + "JOIN referral.to toOrg "
        + "LEFT JOIN referral.from fromOrg "
        + "LEFT JOIN fromOrg.userProfiles userProfile "
        + "WHERE (userProfile = :currentUser OR referral.fromUser = :currentUser) "
        + "GROUP BY toOrg "
        + "ORDER BY toOrg.name ASC")
    List<OrganizationOptionDTO> getMadeToOptionsForCurrentUser(@Param("currentUser") UserProfile currentUser);
}
