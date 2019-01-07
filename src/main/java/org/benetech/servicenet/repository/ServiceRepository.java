package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the Service entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {

    Optional<Service> findOneByExternalDbIdAndProviderName(String externalDbId, String providerName);
}
