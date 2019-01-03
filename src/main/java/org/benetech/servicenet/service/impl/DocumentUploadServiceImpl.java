package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.adapter.DataAdapterFactory;
import org.benetech.servicenet.adapter.SingleDataAdapter;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.converter.FileConverterFactory;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.repository.DocumentUploadRepository;
import org.benetech.servicenet.service.DocumentUploadService;
import org.benetech.servicenet.service.MongoDbService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.DocumentUploadDTO;
import org.benetech.servicenet.service.mapper.DocumentUploadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing DocumentUpload.
 */
@Service
@Transactional
public class DocumentUploadServiceImpl implements DocumentUploadService {

    private final Logger log = LoggerFactory.getLogger(DocumentUploadServiceImpl.class);

    @Autowired
    private DocumentUploadRepository documentUploadRepository;

    @Autowired
    private DocumentUploadMapper documentUploadMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoDbService mongoDbService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public DocumentUploadDTO uploadFile(MultipartFile file, String delimiter, String providerName)
        throws IllegalArgumentException, IOException {

        String parsedDocument = FileConverterFactory.getConverter(file, delimiter).convert(file);
        String parsedDocumentId = mongoDbService.saveParsedDocument(parsedDocument);
        String originalDocumentId = mongoDbService.saveOriginalDocument(file.getBytes());

        DocumentUpload documentUpload = saveForCurrentUser(new DocumentUpload(originalDocumentId, parsedDocumentId));

        return importDataIfNeeded(getRealProviderName(providerName), parsedDocument, documentUpload);
    }

    @Override
    public DocumentUploadDTO uploadApiData(String json, String providerName)
        throws IllegalArgumentException {

        String originalDocumentId = mongoDbService.saveOriginalDocument(json.getBytes());

        DocumentUpload documentUpload = saveForSystemUser(new DocumentUpload(originalDocumentId, null));

        return importDataIfNeeded(providerName, json, documentUpload);
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
        Optional<User> currentUser = userService.getUserWithAuthorities();
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
        Optional<User> currentUser = userService.getSystemUser();
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

    private DocumentUploadDTO importDataIfNeeded(String providerName, String parsedDocument, DocumentUpload documentUpload) {
        Optional<SingleDataAdapter> adapter = new DataAdapterFactory(applicationContext)
            .getSingleDataAdapter(providerName);
        adapter.ifPresent(a -> a.importData(new SingleImportData(parsedDocument, documentUpload, providerName)));
        //TODO: in other case - save in a scheduler queue to be mapped with other dependent files

        return documentUploadMapper.toDto(documentUpload);
    }

    private String getRealProviderName(String currentProviderName) {
        if (userService.isCurrentUserAdmin()) {
            return currentProviderName;
        }

        Optional<User> user = userService.getUserWithAuthorities();
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
}
