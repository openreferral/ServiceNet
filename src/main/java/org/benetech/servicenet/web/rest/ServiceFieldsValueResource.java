package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.service.ServiceFieldsValueService;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.service.dto.ServiceFieldsValueDTO;

import org.benetech.servicenet.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.ServiceFieldsValue}.
 */
@RestController
@RequestMapping("/api")
public class ServiceFieldsValueResource {

    private final Logger log = LoggerFactory.getLogger(ServiceFieldsValueResource.class);

    private static final String ENTITY_NAME = "serviceFieldsValue";

    private final ServiceFieldsValueService serviceFieldsValueService;

    public ServiceFieldsValueResource(ServiceFieldsValueService serviceFieldsValueService) {
        this.serviceFieldsValueService = serviceFieldsValueService;
    }

    /**
     * {@code POST  /service-fields-values} : Create a new serviceFieldsValue.
     *
     * @param serviceFieldsValueDTO the serviceFieldsValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceFieldsValueDTO,
     * or with status {@code 400 (Bad Request)} if the serviceFieldsValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-fields-values")
    public ResponseEntity<ServiceFieldsValueDTO> createServiceFieldsValue(
        @Valid @RequestBody ServiceFieldsValueDTO serviceFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ServiceFieldsValue : {}", serviceFieldsValueDTO);
        if (serviceFieldsValueDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new serviceFieldsValue cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        ServiceFieldsValueDTO result = serviceFieldsValueService.save(serviceFieldsValueDTO);
        return ResponseEntity.created(new URI("/api/service-fields-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-fields-values} : Updates an existing serviceFieldsValue.
     *
     * @param serviceFieldsValueDTO the serviceFieldsValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceFieldsValueDTO,
     * or with status {@code 400 (Bad Request)} if the serviceFieldsValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceFieldsValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-fields-values")
    public ResponseEntity<ServiceFieldsValueDTO> updateServiceFieldsValue(
        @Valid @RequestBody ServiceFieldsValueDTO serviceFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceFieldsValue : {}", serviceFieldsValueDTO);
        if (serviceFieldsValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceFieldsValueDTO result = serviceFieldsValueService.save(serviceFieldsValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceFieldsValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /service-fields-values} : get all the serviceFieldsValues.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceFieldsValues in body.
     */
    @GetMapping("/service-fields-values")
    public List<ServiceFieldsValueDTO> getAllServiceFieldsValues() {
        log.debug("REST request to get all ServiceFieldsValues");
        return serviceFieldsValueService.findAll();
    }

    /**
     * {@code GET  /service-fields-values/:id} : get the "id" serviceFieldsValue.
     *
     * @param id the id of the serviceFieldsValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceFieldsValueDTO, or with
     * status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-fields-values/{id}")
    public ResponseEntity<ServiceFieldsValueDTO> getServiceFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to get ServiceFieldsValue : {}", id);
        Optional<ServiceFieldsValueDTO> serviceFieldsValueDTO = serviceFieldsValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceFieldsValueDTO);
    }

    /**
     * {@code DELETE  /service-fields-values/:id} : delete the "id" serviceFieldsValue.
     *
     * @param id the id of the serviceFieldsValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-fields-values/{id}")
    public ResponseEntity<Void> deleteServiceFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to delete ServiceFieldsValue : {}", id);
        serviceFieldsValueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
