package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.repository.ContactRepository;
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.dto.ContactDTO;
import org.benetech.servicenet.service.mapper.ContactMapper;
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
 * Service Implementation for managing Contact.
 */
@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    public ContactServiceImpl(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    /**
     * Save a contact.
     *
     * @param contactDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContactDTO save(ContactDTO contactDTO) {
        log.debug("Request to save Contact : {}", contactDTO);

        Contact contact = contactMapper.toEntity(contactDTO);
        contact = contactRepository.save(contact);
        return contactMapper.toDto(contact);
    }

    /**
     * Get all the contacts.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContactDTO> findAll() {
        log.debug("Request to get all Contacts");
        return contactRepository.findAll().stream()
            .map(contactMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one contact by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContactDTO> findOne(UUID id) {
        log.debug("Request to get Contact : {}", id);
        return contactRepository.findById(id)
            .map(contactMapper::toDto);
    }

    @Override
    public Optional<Contact> findForExternalDb(String externalDbId, String providerNae) {
        return contactRepository.findOneByExternalDbIdAndProviderName(externalDbId, providerNae);
    }

    /**
     * Delete the contact by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Contact : {}", id);
        contactRepository.deleteById(id);
    }
}
