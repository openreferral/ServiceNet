package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.RequiredDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the RequiredDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequiredDocumentRepository extends JpaRepository<RequiredDocument, UUID> {

    Optional<RequiredDocument> findOneByExternalDbIdAndProviderName(String externalDbId, String providerName);

    Page<RequiredDocument> findAll(Pageable pageable);
}
