package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.service.dto.SystemAccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the systemAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SystemAccountDTO> findAll(Pageable pageable);

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

    /**
     * Find the systemAccount by name.
     *
     * @param name the name of the account
     */
    Optional<SystemAccount> findByName(String name);

}
