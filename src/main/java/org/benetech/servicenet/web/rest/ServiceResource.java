package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.ServiceDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Service.
 */
@RestController
@RequestMapping("/api")
public class ServiceResource {

    private static final String ENTITY_NAME = "service";
    private final Logger log = LoggerFactory.getLogger(ServiceResource.class);
    private final ServiceService serviceService;

    public ServiceResource(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * POST  /services : Create a new service.
     *
     * @param serviceDTO the serviceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceDTO,
     * or with status 400 (Bad Request) if the service has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/services")
    @Timed
    public ResponseEntity<ServiceDTO> createService(@Valid @RequestBody ServiceDTO serviceDTO) throws URISyntaxException {
        log.debug("REST request to save Service : {}", serviceDTO);
        if (serviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new service cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceDTO result = serviceService.save(serviceDTO);
        return ResponseEntity.created(new URI("/api/services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /services : Updates an existing service.
     *
     * @param serviceDTO the serviceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceDTO,
     * or with status 400 (Bad Request) if the serviceDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/services")
    @Timed
    public ResponseEntity<ServiceDTO> updateService(@Valid @RequestBody ServiceDTO serviceDTO) throws URISyntaxException {
        log.debug("REST request to update Service : {}", serviceDTO);
        if (serviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceDTO result = serviceService.save(serviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /services : get all the services.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of services in body
     */
    @GetMapping("/services")
    @Timed
    public List<ServiceDTO> getAllServices(@RequestParam(required = false) String filter) {
        if ("location-is-null".equals(filter)) {
            log.debug("REST request to get all Services where location is null");
            return serviceService.findAllWhereLocationIsNull();
        }
        if ("regularschedule-is-null".equals(filter)) {
            log.debug("REST request to get all Services where regularSchedule is null");
            return serviceService.findAllWhereRegularScheduleIsNull();
        }
        if ("holidayschedule-is-null".equals(filter)) {
            log.debug("REST request to get all Services where holidaySchedule is null");
            return serviceService.findAllWhereHolidayScheduleIsNull();
        }
        if ("funding-is-null".equals(filter)) {
            log.debug("REST request to get all Services where funding is null");
            return serviceService.findAllWhereFundingIsNull();
        }
        if ("eligibility-is-null".equals(filter)) {
            log.debug("REST request to get all Services where eligibility is null");
            return serviceService.findAllWhereEligibilityIsNull();
        }
        log.debug("REST request to get all Services");
        return serviceService.findAll();
    }

    /**
     * GET  /services/:id : get the "id" service.
     *
     * @param id the id of the serviceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/services/{id}")
    @Timed
    public ResponseEntity<ServiceDTO> getService(@PathVariable Long id) {
        log.debug("REST request to get Service : {}", id);
        Optional<ServiceDTO> serviceDTO = serviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceDTO);
    }

    /**
     * DELETE  /services/:id : delete the "id" service.
     *
     * @param id the id of the serviceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/services/{id}")
    @Timed
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        log.debug("REST request to delete Service : {}", id);
        serviceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
