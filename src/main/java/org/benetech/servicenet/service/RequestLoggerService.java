package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.service.dto.RequestLoggerDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.RequestLogger}.
 */
public interface RequestLoggerService {

    /**
     * Save a requestLogger.
     *
     * @param requestLoggerDTO the entity to save.
     * @return the persisted entity.
     */
    RequestLoggerDTO save(RequestLoggerDTO requestLoggerDTO);

    /**
     * Get all the requestLoggers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RequestLoggerDTO> findAll(Pageable pageable);


    /**
     * Get the "id" requestLogger.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RequestLoggerDTO> findOne(UUID id);

    /**
     * Delete the "id" requestLogger.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
