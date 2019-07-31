package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing AccessibilityForDisabilities.
 */
public interface AccessibilityForDisabilitiesService {

    /**
     * Save a accessibilityForDisabilities.
     *
     * @param accessibilityForDisabilitiesDTO the entity to save
     * @return the persisted entity
     */
    AccessibilityForDisabilitiesDTO save(AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO);

    /**
     * Get all the accessibilityForDisabilities.
     *
     * @return the list of entities
     */
    List<AccessibilityForDisabilitiesDTO> findAll();

    /**
     * Get all the accessibilityForDisabilities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AccessibilityForDisabilitiesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" accessibilityForDisabilities.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AccessibilityForDisabilitiesDTO> findOne(UUID id);

    /**
     * Delete the "id" accessibilityForDisabilities.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
