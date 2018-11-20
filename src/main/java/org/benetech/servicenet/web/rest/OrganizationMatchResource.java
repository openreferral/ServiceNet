package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing OrganizationMatch.
 */
@RestController
@RequestMapping("/api")
public class OrganizationMatchResource {

    private static final String ENTITY_NAME = "organizationMatch";
    private final Logger log = LoggerFactory.getLogger(OrganizationMatchResource.class);
    private final OrganizationMatchService organizationMatchService;

    public OrganizationMatchResource(OrganizationMatchService organizationMatchService) {
        this.organizationMatchService = organizationMatchService;
    }

    /**
     * POST  /organization-matches : Create a new organizationMatch.
     *
     * @param organizationMatchDTO the organizationMatchDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new organizationMatchDTO,
     * or with status 400 (Bad Request) if the organizationMatch has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/organization-matches")
    @Timed
    public ResponseEntity<OrganizationMatchDTO> createOrganizationMatch(
        @RequestBody OrganizationMatchDTO organizationMatchDTO) throws URISyntaxException {
        log.debug("REST request to save OrganizationMatch : {}", organizationMatchDTO);
        if (organizationMatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new organizationMatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationMatchDTO result = organizationMatchService.save(organizationMatchDTO);
        return ResponseEntity.created(new URI("/api/organization-matches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /organization-matches : Updates an existing organizationMatch.
     *
     * @param organizationMatchDTO the organizationMatchDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organizationMatchDTO,
     * or with status 400 (Bad Request) if the organizationMatchDTO is not valid,
     * or with status 500 (Internal Server Error) if the organizationMatchDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/organization-matches")
    @Timed
    public ResponseEntity<OrganizationMatchDTO> updateOrganizationMatch(
        @RequestBody OrganizationMatchDTO organizationMatchDTO) throws URISyntaxException {
        log.debug("REST request to update OrganizationMatch : {}", organizationMatchDTO);
        if (organizationMatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationMatchDTO result = organizationMatchService.save(organizationMatchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organizationMatchDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /organization-matches : get all the organizationMatches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of organizationMatches in body
     */
    @GetMapping("/organization-matches")
    @Timed
    public List<OrganizationMatchDTO> getAllOrganizationMatches() {
        log.debug("REST request to get all OrganizationMatches");
        return organizationMatchService.findAll();
    }

    /**
     * GET  /organization-matches/:id : get the "id" organizationMatch.
     *
     * @param id the id of the organizationMatchDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the organizationMatchDTO, or with status 404 (Not Found)
     */
    @GetMapping("/organization-matches/{id}")
    @Timed
    public ResponseEntity<OrganizationMatchDTO> getOrganizationMatch(@PathVariable UUID id) {
        log.debug("REST request to get OrganizationMatch : {}", id);
        Optional<OrganizationMatchDTO> organizationMatchDTO = organizationMatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationMatchDTO);
    }

    /**
     * DELETE  /organization-matches/:id : delete the "id" organizationMatch.
     *
     * @param id the id of the organizationMatchDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/organization-matches/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrganizationMatch(@PathVariable UUID id) {
        log.debug("REST request to delete OrganizationMatch : {}", id);
        organizationMatchService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
