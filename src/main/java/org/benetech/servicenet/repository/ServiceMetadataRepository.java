package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.ServiceMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceMetadata entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceMetadataRepository extends JpaRepository<ServiceMetadata, UUID> {

    Page<ServiceMetadata> findAll(Pageable pageable);
}
