package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.repository.RequiredDocumentRepository;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.dto.RequiredDocumentDTO;
import org.benetech.servicenet.service.mapper.RequiredDocumentMapper;
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
 * Service Implementation for managing RequiredDocument.
 */
@Service
@Transactional
public class RequiredDocumentServiceImpl implements RequiredDocumentService {

    private final Logger log = LoggerFactory.getLogger(RequiredDocumentServiceImpl.class);

    private final RequiredDocumentRepository requiredDocumentRepository;

    private final RequiredDocumentMapper requiredDocumentMapper;

    public RequiredDocumentServiceImpl(RequiredDocumentRepository requiredDocumentRepository,
                                       RequiredDocumentMapper requiredDocumentMapper) {
        this.requiredDocumentRepository = requiredDocumentRepository;
        this.requiredDocumentMapper = requiredDocumentMapper;
    }

    /**
     * Save a requiredDocument.
     *
     * @param requiredDocumentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RequiredDocumentDTO save(RequiredDocumentDTO requiredDocumentDTO) {
        log.debug("Request to save RequiredDocument : {}", requiredDocumentDTO);

        RequiredDocument requiredDocument = requiredDocumentMapper.toEntity(requiredDocumentDTO);
        requiredDocument = requiredDocumentRepository.save(requiredDocument);
        return requiredDocumentMapper.toDto(requiredDocument);
    }

    /**
     * Get all the requiredDocuments.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RequiredDocumentDTO> findAll() {
        log.debug("Request to get all RequiredDocuments");
        return requiredDocumentRepository.findAll().stream()
            .map(requiredDocumentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one requiredDocument by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RequiredDocumentDTO> findOne(UUID id) {
        log.debug("Request to get RequiredDocument : {}", id);
        return requiredDocumentRepository.findById(id)
            .map(requiredDocumentMapper::toDto);
    }

    @Override
    public Optional<RequiredDocument> findForExternalDb(String externalDbId, String providerName) {
        return requiredDocumentRepository.findOneByExternalDbIdAndProviderName(externalDbId, providerName);
    }

    /**
     * Delete the requiredDocument by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete RequiredDocument : {}", id);
        requiredDocumentRepository.deleteById(id);
    }
}
