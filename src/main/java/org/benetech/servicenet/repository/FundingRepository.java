package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Funding;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the Funding entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FundingRepository extends JpaRepository<Funding, UUID> {

    @Query("SELECT funding FROM Funding funding WHERE funding.organization.id = :organizationId")
    Optional<Funding> findOneByOrganizationId(@Param("organizationId") UUID organizationId);

    Page<Funding> findAll(Pageable pageable);
}
