package org.benetech.servicenet.service.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.domain.enumeration.ActionType;
import org.benetech.servicenet.repository.MetadataRepository;
import org.benetech.servicenet.service.MetadataService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.MetadataDTO;
import org.benetech.servicenet.service.mapper.MetadataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Metadata.
 */
@Service
@Transactional
public class MetadataServiceImpl implements MetadataService {

    @Autowired
    private UserService userService;

    private final Logger log = LoggerFactory.getLogger(MetadataServiceImpl.class);

    private final MetadataRepository metadataRepository;

    private final MetadataMapper metadataMapper;

    public MetadataServiceImpl(MetadataRepository metadataRepository, MetadataMapper metadataMapper) {
        this.metadataRepository = metadataRepository;
        this.metadataMapper = metadataMapper;
    }

    @Override
    public MetadataDTO save(MetadataDTO metadataDTO) {
        log.debug("Request to save Metadata : {}", metadataDTO);

        Metadata metadata = metadataMapper.toEntity(metadataDTO);
        metadata = metadataRepository.save(metadata);
        return metadataMapper.toDto(metadata);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Metadata> saveForCurrentOrSystemUser(List<Metadata> metadata) {
        UserProfile userProfile = userService.getCurrentOrSystemUserProfile();
        for (Metadata entry : metadata) {
            entry.setUserProfile(userProfile);
            entry.setLastActionDate(ZonedDateTime.now(ZoneId.systemDefault()));
        }
        return metadataRepository.saveAll(metadata);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetadataDTO> findAll() {
        log.debug("Request to get all Metadata");
        return metadataRepository.findAll().stream()
            .map(metadataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MetadataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Metadata");
        return metadataRepository.findAll(pageable)
            .map(metadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MetadataDTO> findOne(UUID id) {
        log.debug("Request to get Metadata : {}", id);
        return metadataRepository.findById(id)
            .map(metadataMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Metadata : {}", id);
        metadataRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Metadata> findMetadataForConflict(UUID resourceId, String fieldName, String replacementValue) {
        log.debug("Request to get most recent metadata for specific change of entity with resourceId: {}.", resourceId);
        Optional<Metadata> metadataOpt =
            metadataRepository.findFirstByResourceIdAndFieldNameAndReplacementValueOrderByLastActionDateAsc(
                resourceId, fieldName, replacementValue);

        if (metadataOpt.isPresent()) {
            return metadataOpt;
        } else {
            return metadataRepository.findFirstByResourceIdAndFieldNameAndLastActionTypeOrderByLastActionDateAsc(
                resourceId, Constants.ALL_FIELDS, ActionType.CREATE);
        }
    }
}
