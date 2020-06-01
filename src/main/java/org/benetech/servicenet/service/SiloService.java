package org.benetech.servicenet.service;

import java.util.List;
import java.util.UUID;
import org.benetech.servicenet.service.dto.SiloDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.Silo}.
 */
public interface SiloService {

    /**
     * Save a silo.
     *
     * @param siloDTO the entity to save.
     * @return the persisted entity.
     */
    SiloDTO save(SiloDTO siloDTO);

    /**
     * Get all the silos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SiloDTO> findPagedAll(Pageable pageable);

    /**
     * Get all the silos.
     *
     * @return the list of entities.
     */
    List<SiloDTO> findAll();


    /**
     * Get the "id" silo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SiloDTO> findOne(UUID id);

    /**
     * Delete the "id" silo.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
