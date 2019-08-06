package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.OrganizationError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrganizationError entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationErrorRepository extends JpaRepository<OrganizationError, UUID> {

    Page<OrganizationError> findAll(Pageable pageable);
}
