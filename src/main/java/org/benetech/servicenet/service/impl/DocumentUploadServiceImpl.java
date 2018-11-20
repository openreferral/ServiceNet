package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.repository.DocumentUploadRepository;
import org.benetech.servicenet.service.DocumentUploadService;
import org.benetech.servicenet.service.dto.DocumentUploadDTO;
import org.benetech.servicenet.service.mapper.DocumentUploadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final DocumentUploadRepository documentUploadRepository;

    private final DocumentUploadMapper documentUploadMapper;

    public DocumentUploadServiceImpl(DocumentUploadRepository documentUploadRepository,
                                     DocumentUploadMapper documentUploadMapper) {
        this.documentUploadRepository = documentUploadRepository;
        this.documentUploadMapper = documentUploadMapper;
    }

    /**
     * Save a documentUpload.
     *
     * @param documentUploadDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DocumentUploadDTO save(DocumentUploadDTO documentUploadDTO) {
        log.debug("Request to save DocumentUpload : {}", documentUploadDTO);

        DocumentUpload documentUpload = documentUploadMapper.toEntity(documentUploadDTO);
        documentUpload = documentUploadRepository.save(documentUpload);
        return documentUploadMapper.toDto(documentUpload);
    }

    /**
     * Get all the documentUploads.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<DocumentUploadDTO> findAll() {
        log.debug("Request to get all DocumentUploads");
        return documentUploadRepository.findAll().stream()
            .map(documentUploadMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one documentUpload by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentUploadDTO> findOne(UUID id) {
        log.debug("Request to get DocumentUpload : {}", id);
        return documentUploadRepository.findById(id)
            .map(documentUploadMapper::toDto);
    }

    /**
     * Delete the documentUpload by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete DocumentUpload : {}", id);
        documentUploadRepository.deleteById(id);
    }
}
