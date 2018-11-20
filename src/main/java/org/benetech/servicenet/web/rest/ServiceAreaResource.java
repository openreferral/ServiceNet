package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.ServiceAreaService;
import org.benetech.servicenet.service.dto.ServiceAreaDTO;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
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
 * REST controller for managing ServiceArea.
 */
@RestController
@RequestMapping("/api")
public class ServiceAreaResource {

    private static final String ENTITY_NAME = "serviceArea";
    private final Logger log = LoggerFactory.getLogger(ServiceAreaResource.class);
    private final ServiceAreaService serviceAreaService;

    public ServiceAreaResource(ServiceAreaService serviceAreaService) {
        this.serviceAreaService = serviceAreaService;
    }

    /**
     * POST  /service-areas : Create a new serviceArea.
     *
     * @param serviceAreaDTO the serviceAreaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceAreaDTO,
     * or with status 400 (Bad Request) if the serviceArea has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-areas")
    @Timed
    public ResponseEntity<ServiceAreaDTO> createServiceArea(
        @Valid @RequestBody ServiceAreaDTO serviceAreaDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceArea : {}", serviceAreaDTO);
        if (serviceAreaDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceArea cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceAreaDTO result = serviceAreaService.save(serviceAreaDTO);
        return ResponseEntity.created(new URI("/api/service-areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-areas : Updates an existing serviceArea.
     *
     * @param serviceAreaDTO the serviceAreaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceAreaDTO,
     * or with status 400 (Bad Request) if the serviceAreaDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceAreaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/service-areas")
    @Timed
    public ResponseEntity<ServiceAreaDTO> updateServiceArea(
        @Valid @RequestBody ServiceAreaDTO serviceAreaDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceArea : {}", serviceAreaDTO);
        if (serviceAreaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceAreaDTO result = serviceAreaService.save(serviceAreaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceAreaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-areas : get all the serviceAreas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of serviceAreas in body
     */
    @GetMapping("/service-areas")
    @Timed
    public List<ServiceAreaDTO> getAllServiceAreas() {
        log.debug("REST request to get all ServiceAreas");
        return serviceAreaService.findAll();
    }

    /**
     * GET  /service-areas/:id : get the "id" serviceArea.
     *
     * @param id the id of the serviceAreaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceAreaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/service-areas/{id}")
    @Timed
    public ResponseEntity<ServiceAreaDTO> getServiceArea(@PathVariable Long id) {
        log.debug("REST request to get ServiceArea : {}", id);
        Optional<ServiceAreaDTO> serviceAreaDTO = serviceAreaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceAreaDTO);
    }

    /**
     * DELETE  /service-areas/:id : delete the "id" serviceArea.
     *
     * @param id the id of the serviceAreaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-areas/{id}")
    @Timed
    public ResponseEntity<Void> deleteServiceArea(@PathVariable Long id) {
        log.debug("REST request to delete ServiceArea : {}", id);
        serviceAreaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
