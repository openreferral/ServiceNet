package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.Referral;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Referral entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReferralRepository extends JpaRepository<Referral, UUID> {
}
