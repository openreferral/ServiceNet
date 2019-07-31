package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the ServiceTaxonomy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceTaxonomyRepository extends JpaRepository<ServiceTaxonomy, UUID> {

    Optional<ServiceTaxonomy> findOneByExternalDbIdAndProviderName(String externalDbId, String providerName);

    Page<ServiceTaxonomy> findAll(Pageable pageable);
}
