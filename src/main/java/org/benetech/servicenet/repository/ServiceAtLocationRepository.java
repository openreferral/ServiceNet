package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.ServiceAtLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the ServiceAtLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceAtLocationRepository extends JpaRepository<ServiceAtLocation, UUID> {

    Optional<ServiceAtLocation> findOneByExternalDbIdAndProviderName(String externalDbId, String providerName);

    Page<ServiceAtLocation> findAll(Pageable pageable);
}
