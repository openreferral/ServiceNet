package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.repository.MetadataRepository;
import org.benetech.servicenet.service.MetadataService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.MetadataDTO;
import org.benetech.servicenet.service.mapper.MetadataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        User user = userService.getCurrentOrSystemUser();
        for (Metadata entry : metadata) {
            entry.setUser(user);
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
}
