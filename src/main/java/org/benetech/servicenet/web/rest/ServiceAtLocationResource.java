package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.ServiceAtLocationService;
import org.benetech.servicenet.service.dto.ServiceAtLocationDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing ServiceAtLocation.
 */
@RestController
@RequestMapping("/api")
public class ServiceAtLocationResource {

    private static final String ENTITY_NAME = "serviceAtLocation";
    private final Logger log = LoggerFactory.getLogger(ServiceAtLocationResource.class);
    private final ServiceAtLocationService serviceAtLocationService;

    public ServiceAtLocationResource(ServiceAtLocationService serviceAtLocationService) {
        this.serviceAtLocationService = serviceAtLocationService;
    }

    /**
     * POST  /service-at-locations : Create a new serviceAtLocation.
     *
     * @param serviceAtLocationDTO the serviceAtLocationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceAtLocationDTO,
     * or with status 400 (Bad Request) if the serviceAtLocation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-at-locations")
    @Timed
    public ResponseEntity<ServiceAtLocationDTO> createServiceAtLocation(
        @RequestBody ServiceAtLocationDTO serviceAtLocationDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceAtLocation : {}", serviceAtLocationDTO);
        if (serviceAtLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceAtLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceAtLocationDTO result = serviceAtLocationService.save(serviceAtLocationDTO);
        return ResponseEntity.created(new URI("/api/service-at-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-at-locations : Updates an existing serviceAtLocation.
     *
     * @param serviceAtLocationDTO the serviceAtLocationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceAtLocationDTO,
     * or with status 400 (Bad Request) if the serviceAtLocationDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceAtLocationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/service-at-locations")
    @Timed
    public ResponseEntity<ServiceAtLocationDTO> updateServiceAtLocation(
        @RequestBody ServiceAtLocationDTO serviceAtLocationDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceAtLocation : {}", serviceAtLocationDTO);
        if (serviceAtLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceAtLocationDTO result = serviceAtLocationService.save(serviceAtLocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceAtLocationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-at-locations : get all the serviceAtLocations.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of serviceAtLocations in body
     */
    @GetMapping("/service-at-locations")
    @Timed
    public List<ServiceAtLocationDTO> getAllServiceAtLocations(@RequestParam(required = false) String filter) {
        log.debug("REST request to get all ServiceAtLocations");
        return serviceAtLocationService.findAll();
    }

    /**
     * GET  /service-at-locations/:id : get the "id" serviceAtLocation.
     *
     * @param id the id of the serviceAtLocationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceAtLocationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/service-at-locations/{id}")
    @Timed
    public ResponseEntity<ServiceAtLocationDTO> getServiceAtLocation(@PathVariable UUID id) {
        log.debug("REST request to get ServiceAtLocation : {}", id);
        Optional<ServiceAtLocationDTO> serviceAtLocationDTO = serviceAtLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceAtLocationDTO);
    }

    /**
     * DELETE  /service-at-locations/:id : delete the "id" serviceAtLocation.
     *
     * @param id the id of the serviceAtLocationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-at-locations/{id}")
    @Timed
    public ResponseEntity<Void> deleteServiceAtLocation(@PathVariable UUID id) {
        log.debug("REST request to delete ServiceAtLocation : {}", id);
        serviceAtLocationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
