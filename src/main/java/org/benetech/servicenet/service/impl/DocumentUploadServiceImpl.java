package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.converter.AbstractFileConverter;
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

    @Override
    public DocumentUploadDTO uploadFile(MultipartFile file, String delimiter) throws IllegalArgumentException, IOException {

        AbstractFileConverter converter = FileConverterFactory.getConverter(file, delimiter);
        String parsedDocumentId = mongoDbService.saveParsedDocument(converter.convert(file));
        String originalDocumentId = mongoDbService.saveOriginalDocument(file.getBytes());

        return documentUploadMapper.toDto(saveForCurrentUser(new DocumentUpload(originalDocumentId, parsedDocumentId)));
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
}
