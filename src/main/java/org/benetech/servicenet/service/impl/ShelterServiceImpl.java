package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Beds;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.repository.BedsRepository;
import org.benetech.servicenet.repository.ShelterRepository;
import org.benetech.servicenet.service.ShelterService;
import org.benetech.servicenet.service.dto.ShelterDTO;
import org.benetech.servicenet.service.dto.ShelterFiltersDTO;
import org.benetech.servicenet.service.mapper.ShelterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Shelter.
 */
@Service
@Transactional
public class ShelterServiceImpl implements ShelterService {

    private final Logger log = LoggerFactory.getLogger(ShelterServiceImpl.class);

    private final ShelterRepository shelterRepository;

    private final ShelterMapper shelterMapper;

    private final BedsRepository bedsRepository;

    public ShelterServiceImpl(ShelterRepository shelterRepository, ShelterMapper shelterMapper,
        BedsRepository bedsRepository) {
        this.shelterRepository = shelterRepository;
        this.shelterMapper = shelterMapper;
        this.bedsRepository = bedsRepository;
    }

    /**
     * Save a shelter.
     *
     * @param shelterDto the entity to save
     * @return the persisted entity
     */
    @Override
    public ShelterDTO save(ShelterDTO shelterDto) {
        log.debug("Request to save Shelter : {}", shelterDto);

        Shelter shelter = shelterMapper.toEntity(shelterDto);

        Beds beds = shelter.getBeds();
        if (beds != null) {
            beds.setShelter(shelter);
            shelter.setBeds(bedsRepository.save(beds));
        }

        if (shelter.getPhones() != null) {
            Set<Phone> phones = shelter.getPhones().stream()
                .filter(phone -> StringUtils.isNotBlank(phone.getNumber()))
                .collect(Collectors.toSet());
            for (Phone phone : phones) {
                phone.setShelter(shelter);
            }
            shelter.setPhones(phones);
        }

        if (shelter.getEmails() != null) {
            List<String> emails = shelter.getEmails().stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
            shelter.setEmails(emails);
        }

        if (shelter.getId() != null) {
            shelter.setUserProfiles(shelterRepository.findById(shelter.getId()).orElse(shelter).getUserProfiles());
        }

        shelter = shelterRepository.save(shelter);
        return shelterMapper.toGeocodedDto(shelter);
    }

    /**
     * Get all the shelters.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ShelterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Shelters");
        return shelterRepository.findAll(pageable)
            .map(shelterMapper::toDto);
    }

    /**
     * Get all the shelters.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShelterDTO> findAll() {
        log.debug("Request to get all Shelters");
        return shelterRepository.findAll().stream()
            .map(shelterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShelterDTO> search(ShelterFiltersDTO shelterFilters, Pageable pageable) {
        Page<Shelter> shelters = shelterRepository.search(shelterFilters, pageable);

        List<ShelterDTO> shelterDTOs = shelters.stream()
            .map(shelterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
        return new PageImpl<>(
            shelterDTOs,
            PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
            shelters.getTotalElements()
        );
    }

    /**
     * Get one shelter by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ShelterDTO> findOne(UUID id) {
        log.debug("Request to get Shelter : {}", id);
        return shelterRepository.findById(id)
            .map(shelterMapper::toGeocodedDto);
    }

    /**
     * Delete the shelter by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Shelter : {}", id);
        shelterRepository.findById(id).ifPresent(shelter -> {
            shelter.getUserProfiles().forEach(userProfile -> userProfile.getShelters().remove(shelter));
            shelterRepository.delete(shelter);
        });
    }
}
