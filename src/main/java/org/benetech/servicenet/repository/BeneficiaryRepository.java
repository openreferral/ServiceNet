package org.benetech.servicenet.repository;

import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.domain.Beneficiary;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Beneficiary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, UUID> {

    Optional<Beneficiary> findByPhoneNumber(String phoneNumber);

    Optional<Beneficiary> findByIdentifier(Integer identifier);
}
