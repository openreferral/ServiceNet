package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.PaymentAcceptedDTO;

import java.util.List;
import java.util.Optional;

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
     * Get the "id" paymentAccepted.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PaymentAcceptedDTO> findOne(Long id);

    /**
     * Delete the "id" paymentAccepted.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
