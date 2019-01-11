package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.DocumentUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data  repository for the DocumentUpload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentUploadRepository extends JpaRepository<DocumentUpload, UUID> {

    @Query("select document_upload from DocumentUpload document_upload where document_upload.uploader.login = ?#{principal" +
        ".username}")
    List<DocumentUpload> findByUploaderIsCurrentUser();

    DocumentUpload findByParsedDocumentId(String parsedDocumentId);

}
