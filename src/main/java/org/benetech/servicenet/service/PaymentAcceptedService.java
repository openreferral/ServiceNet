package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.PaymentAcceptedDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PaymentAccepted.
 */
public interface PaymentAcceptedService {

    /**
     * Save a paymentAccepted.
     *
     * @param paymentAcceptedDTO the entity to save
     * @return the persisted entity
     */
    PaymentAcceptedDTO save(PaymentAcceptedDTO paymentAcceptedDTO);

    /**
     * Get all the paymentAccepteds.
     *
     * @return the list of entities
     */
    List<PaymentAcceptedDTO> findAll();

    /**
     * Get all the paymentAccepteds.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PaymentAcceptedDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paymentAccepted.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PaymentAcceptedDTO> findOne(UUID id);

    /**
     * Delete the "id" paymentAccepted.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
