package org.benetech.servicenet.service.impl;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.adapter.DataAdapterFactory;
import org.benetech.servicenet.adapter.MultipleDataAdapter;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.shared.model.FileInfo;
import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.converter.ImportData;
import org.benetech.servicenet.converter.FileConverterFactory;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.repository.DocumentUploadRepository;
import org.benetech.servicenet.service.DataImportReportService;
import org.benetech.servicenet.service.DocumentUploadService;
import org.benetech.servicenet.service.MongoDbService;
import org.benetech.servicenet.service.TransactionSynchronizationService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.DocumentUploadDTO;
import org.benetech.servicenet.service.mapper.DocumentUploadMapper;
import org.benetech.servicenet.util.BsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing DocumentUpload.
 */
@Service
public class DocumentUploadServiceImpl implements DocumentUploadService {

    private final Logger log = LoggerFactory.getLogger(DocumentUploadServiceImpl.class);

    @Autowired
    private DocumentUploadRepository documentUploadRepository;

    @Autowired
    private DocumentUploadMapper documentUploadMapper;

    @Autowired
    private DataImportReportService dataImportReportService;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoDbService mongoDbService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TransactionSynchronizationService transactionSynchronizationService;

    @Override
    public DocumentUploadDTO uploadFile(MultipartFile file, String delimiter, String providerName)
        throws IllegalArgumentException, IOException {
        DataImportReport report = new DataImportReport().startDate(ZonedDateTime.now())
            .systemAccount(providerName);

        String originalDocumentId = mongoDbService.saveOriginalDocument(file.getBytes(), file.getContentType(), delimiter);

        DocumentUpload documentUpload = new DocumentUpload(originalDocumentId, null);
        documentUpload.setFilename(file.getOriginalFilename());
        documentUpload.setDelimiter(delimiter);
        documentUpload = saveForCurrentUser(documentUpload);
        report.setDocumentUpload(documentUpload);

        return importDataIfNeeded(getRealProviderName(providerName), report, true);
    }

    @Override
    public DocumentUploadDTO uploadApiData(String json, String providerName, DataImportReport report)
        throws IllegalArgumentException, IOException {

        String originalDocumentId = mongoDbService.saveOriginalDocument(json.getBytes(), "json", null);
        DocumentUpload documentUpload = saveForSystemUser(new DocumentUpload(originalDocumentId, null));
        report.setDocumentUpload(documentUpload);

        return importDataIfNeeded(providerName, report, false);
    }

    @Override
    public boolean processFiles(final List<FileInfo> fileInfoList, final String providerName)
        throws IOException {
        Optional<MultipleDataAdapter> adapter = new DataAdapterFactory(applicationContext)
            .getMultipleDataAdapter(providerName);
        if (adapter.isEmpty()) {
            // No need to process files again if provider is not of MultipleDataAdapter type
            return true;
        }

        DataImportReport report = new DataImportReport().startDate(ZonedDateTime.now()).systemAccount(providerName);
        List<String> parsedDocuments = new ArrayList<>();
        List<DocumentUpload> documentUploads = new ArrayList<>();

        fillLists(fileInfoList, parsedDocuments, documentUploads);

        long startTime = System.currentTimeMillis();
        log.info("Data upload for " + providerName + " has started");
        DataImportReport reportToSave = adapter
            .map(a -> a.importData(new MultipleImportData(parsedDocuments, documentUploads, report, providerName,
                true)))
            .orElse(report);
        transactionSynchronizationService.registerSynchronizationOfMatchingOrganizations();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        //TODO: Remove time counting logic (#264)
        log.info("Data upload for " + providerName + " took: " + elapsedTime + "ms");
        saveReport(reportToSave);

        return true;
    }

    @Override
    public DocumentUploadDTO save(DocumentUploadDTO documentUploadDTO) {
        log.debug("Request to save DocumentUpload : {}", documentUploadDTO);

        DocumentUpload documentUpload = documentUploadMapper.toEntity(documentUploadDTO);
        documentUpload = documentUploadRepository.save(documentUpload);
        return documentUploadMapper.toDto(documentUpload);
    }

    @Override
    public DocumentUpload saveForCurrentUser(DocumentUpload documentUpload) {
        Optional<UserProfile> currentUser = userService.getCurrentUserProfileOptional();
        if (currentUser.isPresent()) {
            documentUpload.setDateUploaded(ZonedDateTime.now(ZoneId.systemDefault()));
            documentUpload.setUploader(currentUser.get());
            return documentUploadRepository.save(documentUpload);
        } else {
            throw new IllegalStateException("No current user found");
        }
    }

    @Override
    public DocumentUpload saveForSystemUser(DocumentUpload documentUpload) {
        Optional<UserProfile> currentUser = userService.getSystemUserProfile();
        if (currentUser.isPresent()) {
            documentUpload.setDateUploaded(ZonedDateTime.now(ZoneId.systemDefault()));
            documentUpload.setUploader(currentUser.get());
            return documentUploadRepository.save(documentUpload);
        } else {
            throw new IllegalStateException("No system user found");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentUploadDTO> findAll() {
        log.debug("Request to get all DocumentUploads");
        return documentUploadRepository.findAll().stream()
            .map(documentUploadMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentUploadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentUploads");
        return documentUploadRepository.findAll(pageable)
            .map(documentUploadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentUploadDTO> findOne(UUID id) {
        log.debug("Request to get DocumentUpload : {}", id);
        return documentUploadRepository.findById(id)
            .map(documentUploadMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete DocumentUpload : {}", id);
        documentUploadRepository.deleteById(id);
    }

    private DocumentUploadDTO importDataIfNeeded(String providerName, DataImportReport report, boolean isFileUpload)
        throws IOException {
        Optional<SingleDataAdapter> adapter = new DataAdapterFactory(applicationContext)
            .getSingleDataAdapter(providerName);
        DocumentUpload documentUpload = report.getDocumentUpload();
        Object doc = mongoDbService.findOriginalDocumentById(documentUpload.getOriginalDocumentId());
        ImportData importData = new ImportData();
        if (documentUpload.getFilename() == null) {
            // already got a json as an original doc
            importData.setJson(BsonUtils.docToString(doc));
        } else {
            importData = FileConverterFactory.getConverter(
                documentUpload.getFilename(), documentUpload.getDelimiter()).convert(doc);
        }

        ImportData finalImportData = importData;
        DataImportReport reportToSave = adapter
            .map((a) -> {
                long startTime = System.currentTimeMillis();
                log.info("Data upload for " + providerName + " has started");
                SingleImportData singleImportData = finalImportData.getTemporaryFile() == null ?
                    new SingleImportData(finalImportData.getJson(), report, providerName, isFileUpload)
                    : new SingleImportData(finalImportData.getTemporaryFile(), report, providerName, isFileUpload);
                DataImportReport importReport = a.importData(singleImportData);

                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                log.info("Data upload for " + providerName + " took: " + elapsedTime + "ms");
                return saveReport(importReport);
            })
            .orElse(report);

        return documentUploadMapper.toDto(reportToSave.getDocumentUpload());
    }

    private String getRealProviderName(String currentProviderName) {
        if (userService.isCurrentUserAdmin()) {
            return currentProviderName;
        }

        Optional<UserProfile> user = userService.getCurrentUserProfileOptional();
        if (user.isPresent()) {
            if (user.get().getSystemAccount() != null) {
                return user.get().getSystemAccount().getName();
            } else {
                throw new IllegalStateException("No System Account is attached to the user");
            }
        } else {
            throw new IllegalStateException("User has to be authorized to determine the provider");
        }
    }

    private void fillLists(List<FileInfo> fileInfoList, List<String> parsedDocuments, List<DocumentUpload> documentUploads)
        throws IOException {
        for (FileInfo fileInfo : fileInfoList) {
            Object doc = mongoDbService.findOriginalDocumentById(fileInfo.getOriginalDocumentId());

            DocumentUpload docUpload = documentUploadRepository.findByOriginalDocumentId(fileInfo.getOriginalDocumentId());
            if (docUpload.getFilename() == null) {
                docUpload.setFilename(fileInfo.getFilename() + ".csv");
            }
            documentUploads.add(docUpload);

            if (docUpload.getFilename() == null) {
                parsedDocuments.add(BsonUtils.docToString(doc));
            } else {
                ImportData importData = FileConverterFactory.getConverter(
                    docUpload.getFilename(), docUpload.getDelimiter()).convert(doc);
                parsedDocuments.add(importData.getJson());
            }
        }
    }

    private DataImportReport saveReport(DataImportReport report) {
        report.setEndDate(ZonedDateTime.now());
        return dataImportReportService.save(report);
    }
}
