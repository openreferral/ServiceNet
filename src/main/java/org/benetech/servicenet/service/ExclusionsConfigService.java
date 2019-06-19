package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.service.dto.ExclusionsConfigDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.ExclusionsConfig}.
 */
public interface ExclusionsConfigService {

    /**
     * Save a exclusionsConfig.
     *
     * @param exclusionsConfigDTO the entity to save.
     * @return the persisted entity.
     */
    ExclusionsConfigDTO save(ExclusionsConfigDTO exclusionsConfigDTO);

    /**
     * Get all the exclusionsConfigs.
     *
     * @return the list of entities.
     */
    List<ExclusionsConfigDTO> findAll();

    Map<UUID, ExclusionsConfig> getAllBySystemAccountId();

    /**
     * Get the "id" exclusionsConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExclusionsConfigDTO> findOne(UUID id);

    /**
     * Delete the "id" exclusionsConfig.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
