package org.benetech.servicenet.service;

import java.util.UUID;
import org.benetech.servicenet.domain.Beneficiary;
import org.benetech.servicenet.service.dto.BeneficiaryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.Beneficiary}.
 */
public interface BeneficiaryService {

    /**
     * Save a beneficiary.
     *
     * @param beneficiaryDTO the entity to save.
     * @return the persisted entity.
     */
    BeneficiaryDTO save(BeneficiaryDTO beneficiaryDTO);

    /**
     * Get all the beneficiaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BeneficiaryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" beneficiary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BeneficiaryDTO> findOne(UUID id);

    /**
     * Get the "id" beneficiary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Beneficiary> getOne(UUID id);

    /**
     * Delete the "id" beneficiary.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    Beneficiary findOrCreateByPhoneNumber(String phoneNumber);
}
