package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.RequiredDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RequiredDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequiredDocumentRepository extends JpaRepository<RequiredDocument, Long> {

}
