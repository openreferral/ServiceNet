package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.PaymentAcceptedService;
import org.benetech.servicenet.service.dto.PaymentAcceptedDTO;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing PaymentAccepted.
 */
@RestController
@RequestMapping("/api")
public class PaymentAcceptedResource {

    private static final String ENTITY_NAME = "paymentAccepted";
    private final Logger log = LoggerFactory.getLogger(PaymentAcceptedResource.class);
    private final PaymentAcceptedService paymentAcceptedService;

    public PaymentAcceptedResource(PaymentAcceptedService paymentAcceptedService) {
        this.paymentAcceptedService = paymentAcceptedService;
    }

    /**
     * POST  /payment-accepteds : Create a new paymentAccepted.
     *
     * @param paymentAcceptedDTO the paymentAcceptedDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentAcceptedDTO,
     * or with status 400 (Bad Request) if the paymentAccepted has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/payment-accepteds")
    @Timed
    public ResponseEntity<PaymentAcceptedDTO> createPaymentAccepted(
        @RequestBody PaymentAcceptedDTO paymentAcceptedDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentAccepted : {}", paymentAcceptedDTO);
        if (paymentAcceptedDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentAccepted cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentAcceptedDTO result = paymentAcceptedService.save(paymentAcceptedDTO);
        return ResponseEntity.created(new URI("/api/payment-accepteds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payment-accepteds : Updates an existing paymentAccepted.
     *
     * @param paymentAcceptedDTO the paymentAcceptedDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentAcceptedDTO,
     * or with status 400 (Bad Request) if the paymentAcceptedDTO is not valid,
     * or with status 500 (Internal Server Error) if the paymentAcceptedDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/payment-accepteds")
    @Timed
    public ResponseEntity<PaymentAcceptedDTO> updatePaymentAccepted(
        @RequestBody PaymentAcceptedDTO paymentAcceptedDTO) throws URISyntaxException {
        log.debug("REST request to update PaymentAccepted : {}", paymentAcceptedDTO);
        if (paymentAcceptedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentAcceptedDTO result = paymentAcceptedService.save(paymentAcceptedDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentAcceptedDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-accepteds : get all the paymentAccepteds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of paymentAccepteds in body
     */
    @GetMapping("/payment-accepteds")
    @Timed
    public List<PaymentAcceptedDTO> getAllPaymentAccepteds() {
        log.debug("REST request to get all PaymentAccepteds");
        return paymentAcceptedService.findAll();
    }

    /**
     * GET  /payment-accepteds/:id : get the "id" paymentAccepted.
     *
     * @param id the id of the paymentAcceptedDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentAcceptedDTO, or with status 404 (Not Found)
     */
    @GetMapping("/payment-accepteds/{id}")
    @Timed
    public ResponseEntity<PaymentAcceptedDTO> getPaymentAccepted(@PathVariable UUID id) {
        log.debug("REST request to get PaymentAccepted : {}", id);
        Optional<PaymentAcceptedDTO> paymentAcceptedDTO = paymentAcceptedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentAcceptedDTO);
    }

    /**
     * DELETE  /payment-accepteds/:id : delete the "id" paymentAccepted.
     *
     * @param id the id of the paymentAcceptedDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/payment-accepteds/{id}")
    @Timed
    public ResponseEntity<Void> deletePaymentAccepted(@PathVariable UUID id) {
        log.debug("REST request to delete PaymentAccepted : {}", id);
        paymentAcceptedService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
