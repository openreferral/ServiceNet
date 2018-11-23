package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.SystemAccountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing SystemAccount.
 */
public interface SystemAccountService {

    /**
     * Save a systemAccount.
     *
     * @param systemAccountDTO the entity to save
     * @return the persisted entity
     */
    SystemAccountDTO save(SystemAccountDTO systemAccountDTO);

    /**
     * Get all the systemAccounts.
     *
     * @return the list of entities
     */
    List<SystemAccountDTO> findAll();


    /**
     * Get the "id" systemAccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SystemAccountDTO> findOne(UUID id);

    /**
     * Delete the "id" systemAccount.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
