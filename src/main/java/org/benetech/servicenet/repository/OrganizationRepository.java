package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the Organization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

}
