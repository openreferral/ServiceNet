package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;

import java.util.List;
import java.util.Optional;

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
     * Get the "id" accessibilityForDisabilities.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AccessibilityForDisabilitiesDTO> findOne(Long id);

    /**
     * Delete the "id" accessibilityForDisabilities.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
