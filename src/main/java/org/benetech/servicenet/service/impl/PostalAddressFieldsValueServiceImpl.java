package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.PostalAddressFieldsValue;
import org.benetech.servicenet.repository.PostalAddressFieldsValueRepository;
import org.benetech.servicenet.service.PostalAddressFieldsValueService;
import org.benetech.servicenet.service.dto.PostalAddressFieldsValueDTO;
import org.benetech.servicenet.service.mapper.PostalAddressFieldsValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PostalAddressFieldsValue}.
 */
@Service
@Transactional
public class PostalAddressFieldsValueServiceImpl implements PostalAddressFieldsValueService {

    private final Logger log = LoggerFactory.getLogger(PostalAddressFieldsValueServiceImpl.class);

    private final PostalAddressFieldsValueRepository postalAddressFieldsValueRepository;

    private final PostalAddressFieldsValueMapper postalAddressFieldsValueMapper;

    public PostalAddressFieldsValueServiceImpl(
        PostalAddressFieldsValueRepository postalAddressFieldsValueRepository,
        PostalAddressFieldsValueMapper postalAddressFieldsValueMapper
    ) {
        this.postalAddressFieldsValueRepository = postalAddressFieldsValueRepository;
        this.postalAddressFieldsValueMapper = postalAddressFieldsValueMapper;
    }

    /**
     * Save a postalAddressFieldsValue.
     *
     * @param postalAddressFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PostalAddressFieldsValueDTO save(PostalAddressFieldsValueDTO postalAddressFieldsValueDTO) {
        log.debug("Request to save PostalAddressFieldsValue : {}", postalAddressFieldsValueDTO);
        PostalAddressFieldsValue postalAddressFieldsValue = postalAddressFieldsValueMapper
            .toEntity(postalAddressFieldsValueDTO);
        postalAddressFieldsValue = postalAddressFieldsValueRepository.save(postalAddressFieldsValue);
        return postalAddressFieldsValueMapper.toDto(postalAddressFieldsValue);
    }

    /**
     * Get all the postalAddressFieldsValues.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PostalAddressFieldsValueDTO> findAll() {
        log.debug("Request to get all PostalAddressFieldsValues");
        return postalAddressFieldsValueRepository.findAll().stream()
            .map(postalAddressFieldsValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the postalAddressFieldsValues.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostalAddressFieldsValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostalAddressFieldsValues");
        return postalAddressFieldsValueRepository.findAll(pageable)
            .map(postalAddressFieldsValueMapper::toDto);
    }

    /**
     * Get one postalAddressFieldsValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PostalAddressFieldsValueDTO> findOne(UUID id) {
        log.debug("Request to get PostalAddressFieldsValue : {}", id);
        return postalAddressFieldsValueRepository.findById(id)
            .map(postalAddressFieldsValueMapper::toDto);
    }

    /**
     * Delete the postalAddressFieldsValue by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PostalAddressFieldsValue : {}", id);
        postalAddressFieldsValueRepository.deleteById(id);
    }
}
