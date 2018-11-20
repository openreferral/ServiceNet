package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.PaymentAccepted;
import org.benetech.servicenet.repository.PaymentAcceptedRepository;
import org.benetech.servicenet.service.PaymentAcceptedService;
import org.benetech.servicenet.service.dto.PaymentAcceptedDTO;
import org.benetech.servicenet.service.mapper.PaymentAcceptedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PaymentAccepted.
 */
@Service
@Transactional
public class PaymentAcceptedServiceImpl implements PaymentAcceptedService {

    private final Logger log = LoggerFactory.getLogger(PaymentAcceptedServiceImpl.class);

    private final PaymentAcceptedRepository paymentAcceptedRepository;

    private final PaymentAcceptedMapper paymentAcceptedMapper;

    public PaymentAcceptedServiceImpl(PaymentAcceptedRepository paymentAcceptedRepository,
                                      PaymentAcceptedMapper paymentAcceptedMapper) {
        this.paymentAcceptedRepository = paymentAcceptedRepository;
        this.paymentAcceptedMapper = paymentAcceptedMapper;
    }

    /**
     * Save a paymentAccepted.
     *
     * @param paymentAcceptedDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PaymentAcceptedDTO save(PaymentAcceptedDTO paymentAcceptedDTO) {
        log.debug("Request to save PaymentAccepted : {}", paymentAcceptedDTO);

        PaymentAccepted paymentAccepted = paymentAcceptedMapper.toEntity(paymentAcceptedDTO);
        paymentAccepted = paymentAcceptedRepository.save(paymentAccepted);
        return paymentAcceptedMapper.toDto(paymentAccepted);
    }

    /**
     * Get all the paymentAccepteds.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaymentAcceptedDTO> findAll() {
        log.debug("Request to get all PaymentAccepteds");
        return paymentAcceptedRepository.findAll().stream()
            .map(paymentAcceptedMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one paymentAccepted by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentAcceptedDTO> findOne(Long id) {
        log.debug("Request to get PaymentAccepted : {}", id);
        return paymentAcceptedRepository.findById(id)
            .map(paymentAcceptedMapper::toDto);
    }

    /**
     * Delete the paymentAccepted by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentAccepted : {}", id);
        paymentAcceptedRepository.deleteById(id);
    }
}
