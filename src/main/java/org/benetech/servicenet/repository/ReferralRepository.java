package org.benetech.servicenet.repository;

import java.util.List;
import java.util.UUID;
import org.benetech.servicenet.domain.Referral;

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

    @Query("SELECT referral FROM Referral referral WHERE referral.beneficiary.id = :beneficiaryId AND referral.to.id = :cboId")
    List<Referral> findAllByBeneficiaryIdAndReferredTo(@Param("beneficiaryId") UUID beneficiaryId, @Param("cboId") UUID cboId);
}
