package org.benetech.servicenet.service.impl;

import java.util.UUID;
import org.benetech.servicenet.service.ContactDetailsFieldsValueService;
import org.benetech.servicenet.domain.ContactDetailsFieldsValue;
import org.benetech.servicenet.repository.ContactDetailsFieldsValueRepository;
import org.benetech.servicenet.service.dto.ContactDetailsFieldsValueDTO;
import org.benetech.servicenet.service.mapper.ContactDetailsFieldsValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ContactDetailsFieldsValue}.
 */
@Service
@Transactional
public class ContactDetailsFieldsValueServiceImpl implements ContactDetailsFieldsValueService {

    private final Logger log = LoggerFactory.getLogger(ContactDetailsFieldsValueServiceImpl.class);

    private final ContactDetailsFieldsValueRepository contactDetailsFieldsValueRepository;

    private final ContactDetailsFieldsValueMapper contactDetailsFieldsValueMapper;

    public ContactDetailsFieldsValueServiceImpl(
        ContactDetailsFieldsValueRepository contactDetailsFieldsValueRepository,
        ContactDetailsFieldsValueMapper contactDetailsFieldsValueMapper
    ) {
        this.contactDetailsFieldsValueRepository = contactDetailsFieldsValueRepository;
        this.contactDetailsFieldsValueMapper = contactDetailsFieldsValueMapper;
    }

    /**
     * Save a contactDetailsFieldsValue.
     *
     * @param contactDetailsFieldsValueDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ContactDetailsFieldsValueDTO save(ContactDetailsFieldsValueDTO contactDetailsFieldsValueDTO) {
        log.debug("Request to save ContactDetailsFieldsValue : {}", contactDetailsFieldsValueDTO);
        ContactDetailsFieldsValue contactDetailsFieldsValue = contactDetailsFieldsValueMapper
            .toEntity(contactDetailsFieldsValueDTO);
        contactDetailsFieldsValue = contactDetailsFieldsValueRepository.save(contactDetailsFieldsValue);
        return contactDetailsFieldsValueMapper.toDto(contactDetailsFieldsValue);
    }

    /**
     * Get all the contactDetailsFieldsValues.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContactDetailsFieldsValueDTO> findAll() {
        log.debug("Request to get all ContactDetailsFieldsValues");
        return contactDetailsFieldsValueRepository.findAll().stream()
            .map(contactDetailsFieldsValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one contactDetailsFieldsValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContactDetailsFieldsValueDTO> findOne(UUID id) {
        log.debug("Request to get ContactDetailsFieldsValue : {}", id);
        return contactDetailsFieldsValueRepository.findById(id)
            .map(contactDetailsFieldsValueMapper::toDto);
    }

    /**
     * Delete the contactDetailsFieldsValue by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete ContactDetailsFieldsValue : {}", id);
        contactDetailsFieldsValueRepository.deleteById(id);
    }
}
