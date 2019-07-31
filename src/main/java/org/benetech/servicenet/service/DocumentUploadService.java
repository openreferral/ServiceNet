package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.adapter.shared.model.FileInfo;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.service.dto.DocumentUploadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing DocumentUpload.
 */
public interface DocumentUploadService {

    /**
     * Save a file in NoSQL database and information about it.
     *
     * @param file file to be saved
     * @param delimiter delimiter used to separate entry values (if needed)
     * @param providerName name of the provider, to correctly persist the data
     * @return entity with information about the uploaded file
     * @throws IOException if there's problem with reading the file
     * @throws IllegalArgumentException if file type is not supported
     */
    DocumentUploadDTO uploadFile(MultipartFile file, String delimiter, String providerName) throws IOException,
        IllegalArgumentException;

    /**
     * Processes files after upload by importing data included in those files to database.
     * @param fileInfoList list containing necessary file information
     * @param providerName name of selected provider
     * @return true success, false otherwise
     */
    boolean processFiles(List<FileInfo> fileInfoList, String providerName);

    /**
     * Save a result of API data collection in NoSQL database and information about it.
     *
     * @param json datato be saved
     * @param providerName name of the provider, to correctly persist the data
     * @param report report that should contain startDate of when the upload was started
     * @return entity with information about the uploaded file
     * @throws IllegalArgumentException if file type is not supported
     */
    DocumentUploadDTO uploadApiData(String json, String providerName, DataImportReport report)
        throws IllegalArgumentException;

    /**
     * Save a documentUpload.
     *
     * @param documentUploadDTO the entity to save
     * @return the persisted entity
     */
    DocumentUploadDTO save(DocumentUploadDTO documentUploadDTO);

    /**
     * Save a documentUpload, with reference to current user.
     *
     * @param documentUpload the entity to save
     * @return the persisted entity
     */
    DocumentUpload saveForCurrentUser(DocumentUpload documentUpload);

    /**
     * Save a documentUpload, with reference to the system user.
     *
     * @param documentUpload the entity to save
     * @return the persisted entity
     */
    DocumentUpload saveForSystemUser(DocumentUpload documentUpload);

    /**
     * Get all the documentUploads.
     *
     * @return the list of entities
     */
    List<DocumentUploadDTO> findAll();

    /**
     * Get all the conflicts on page.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DocumentUploadDTO> findAll(Pageable pageable);
    
    /**
     * Get the "id" documentUpload.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DocumentUploadDTO> findOne(UUID id);

    /**
     * Delete the "id" documentUpload.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
