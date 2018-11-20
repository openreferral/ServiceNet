package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.DocumentUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DocumentUpload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentUploadRepository extends JpaRepository<DocumentUpload, Long> {

}
