package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.LanguageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing Language.
 */
public interface LanguageService {

    /**
     * Save a language.
     *
     * @param languageDTO the entity to save
     * @return the persisted entity
     */
    LanguageDTO save(LanguageDTO languageDTO);

    /**
     * Get all the languages.
     *
     * @return the list of entities
     */
    List<LanguageDTO> findAll();

    /**
     * Get all the languages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LanguageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" language.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LanguageDTO> findOne(UUID id);

    /**
     * Delete the "id" language.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
