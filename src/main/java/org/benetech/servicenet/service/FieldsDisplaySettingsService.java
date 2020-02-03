package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.service.dto.FieldsDisplaySettingsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.FieldsDisplaySettings}.
 */
public interface FieldsDisplaySettingsService {

    /**
     * Save a fieldsDisplaySettings.
     *
     * @param fieldsDisplaySettingsDTO the entity to save.
     * @return the persisted entity.
     */
    FieldsDisplaySettingsDTO save(FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO);

    /**
     * Get all the fieldsDisplaySettings.
     *
     * @return the list of entities.
     */
    List<FieldsDisplaySettingsDTO> findAll();

    /**
     * Get the "id" fieldsDisplaySettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FieldsDisplaySettingsDTO> findOne(UUID id);

    /**
     * Delete the "id" fieldsDisplaySettings.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
