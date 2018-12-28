package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the Organization entity.
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    @Query("SELECT org FROM Organization org WHERE org.account.id = :ownerId")
    List<Organization> findAllWithOwnerId(@Param("ownerId") UUID ownerId);
}
