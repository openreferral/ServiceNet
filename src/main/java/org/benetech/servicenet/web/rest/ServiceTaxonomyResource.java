package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.dto.ServiceTaxonomyDTO;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * REST controller for managing ServiceTaxonomy.
 */
@RestController
@RequestMapping("/api")
public class ServiceTaxonomyResource {

    private static final String ENTITY_NAME = "serviceTaxonomy";
    private final Logger log = LoggerFactory.getLogger(ServiceTaxonomyResource.class);
    private final ServiceTaxonomyService serviceTaxonomyService;

    public ServiceTaxonomyResource(ServiceTaxonomyService serviceTaxonomyService) {
        this.serviceTaxonomyService = serviceTaxonomyService;
    }

    /**
     * POST  /service-taxonomies : Create a new serviceTaxonomy.
     *
     * @param serviceTaxonomyDTO the serviceTaxonomyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceTaxonomyDTO,
     * or with status 400 (Bad Request) if the serviceTaxonomy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-taxonomies")
    @Timed
    public ResponseEntity<ServiceTaxonomyDTO> createServiceTaxonomy(
        @RequestBody ServiceTaxonomyDTO serviceTaxonomyDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceTaxonomy : {}", serviceTaxonomyDTO);
        if (serviceTaxonomyDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceTaxonomy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceTaxonomyDTO result = serviceTaxonomyService.save(serviceTaxonomyDTO);
        return ResponseEntity.created(new URI("/api/service-taxonomies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-taxonomies : Updates an existing serviceTaxonomy.
     *
     * @param serviceTaxonomyDTO the serviceTaxonomyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceTaxonomyDTO,
     * or with status 400 (Bad Request) if the serviceTaxonomyDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceTaxonomyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/service-taxonomies")
    @Timed
    public ResponseEntity<ServiceTaxonomyDTO> updateServiceTaxonomy(
        @RequestBody ServiceTaxonomyDTO serviceTaxonomyDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceTaxonomy : {}", serviceTaxonomyDTO);
        if (serviceTaxonomyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ServiceTaxonomyDTO result = serviceTaxonomyService.save(serviceTaxonomyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceTaxonomyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-taxonomies : get all the serviceTaxonomies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviceTaxonomies in body
     */
    @GetMapping("/service-taxonomies")
    @Timed
    public ResponseEntity<List<ServiceTaxonomyDTO>> getAllServiceTaxonomies(Pageable pageable) {
        log.debug("REST request to get all ServiceTaxonomies");
        Page<ServiceTaxonomyDTO> page = serviceTaxonomyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-taxonomies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /service-taxonomies/:id : get the "id" serviceTaxonomy.
     *
     * @param id the id of the serviceTaxonomyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceTaxonomyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/service-taxonomies/{id}")
    @Timed
    public ResponseEntity<ServiceTaxonomyDTO> getServiceTaxonomy(@PathVariable UUID id) {
        log.debug("REST request to get ServiceTaxonomy : {}", id);
        Optional<ServiceTaxonomyDTO> serviceTaxonomyDTO = serviceTaxonomyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceTaxonomyDTO);
    }

    /**
     * DELETE  /service-taxonomies/:id : delete the "id" serviceTaxonomy.
     *
     * @param id the id of the serviceTaxonomyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-taxonomies/{id}")
    @Timed
    public ResponseEntity<Void> deleteServiceTaxonomy(@PathVariable UUID id) {
        log.debug("REST request to delete ServiceTaxonomy : {}", id);
        serviceTaxonomyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
