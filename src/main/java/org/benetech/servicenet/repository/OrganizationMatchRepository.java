package org.benetech.servicenet.repository;

import java.util.Optional;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data  repository for the OrganizationMatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationMatchRepository extends JpaRepository<OrganizationMatch, UUID> {

    @Query("SELECT orgMatch FROM OrganizationMatch orgMatch WHERE orgMatch.organizationRecord.id = :organizationRecordId")
    List<OrganizationMatch> findAllByOrganizationRecordId(@Param("organizationRecordId") UUID uuid);

    @Query("SELECT orgMatch FROM OrganizationMatch orgMatch WHERE orgMatch.partnerVersion.id = :organizationRecordId")
    List<OrganizationMatch> findAllByPartnerVersionId(@Param("organizationRecordId") UUID uuid);

    List<OrganizationMatch> findAllByOrganizationRecordIdAndDismissed(UUID uuid, Boolean dismissed);

    List<OrganizationMatch> findAllByPartnerVersionIdAndDismissed(UUID uuid, Boolean dismissed);

    List<OrganizationMatch> findAllByHidden(Boolean hidden);

    @Query("SELECT orgMatch FROM OrganizationMatch orgMatch WHERE orgMatch.organizationRecord.id = :organizationRecordId AND"
        + " orgMatch.hidden = :hidden ORDER BY orgMatch.partnerVersion.lastVerifiedOn DESC NULLS LAST")
    List<OrganizationMatch> findAllByOrganizationRecordIdAndHidden(
        @Param("organizationRecordId") UUID uuid, @Param("hidden") Boolean hidden);

    Page<OrganizationMatch> findAll(Pageable pageable);

    Optional<OrganizationMatch> findByOrganizationRecordAndPartnerVersion(
        Organization organizationRecord, Organization partnerVersion);
}
