package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.dto.DismissMatchDTO;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
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
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of organizationMatches in body
     */
    @GetMapping("/organization-matches")
    @Timed
    public ResponseEntity<List<OrganizationMatchDTO>> getAllOrganizationMatches(Pageable pageable) {
        log.debug("REST request to get all OrganizationMatches");
        Page<OrganizationMatchDTO> page = organizationMatchService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-matches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /organization-matches/hidden : get all the hidden organizationMatches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of organizationMatches in body
     */
    @GetMapping("/organization-matches/hidden")
    @Timed
    public List<OrganizationMatchDTO> getHiddenOrganizationMatches() {
        log.debug("REST request to get all hidden OrganizationMatches");
        return organizationMatchService.findAllHiddenOrganizationMatches();
    }

    /**
     * GET  /organization-matches/hidden : get all the not hidden organizationMatches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of organizationMatches in body
     */
    @GetMapping("/organization-matches/notHidden")
    @Timed
    public List<OrganizationMatchDTO> getNotHiddenOrganizationMatches() {
        log.debug("REST request to get all hidden OrganizationMatches");
        return organizationMatchService.findAllNotHiddenOrganizationMatches();
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

    @GetMapping("/organization-matches/organization/{id}")
    @Timed
    public List<OrganizationMatchDTO> getOrganizationMatchesForOrganization(@PathVariable UUID id) {
        return organizationMatchService.findAllForOrganization(id);
    }

    @GetMapping("/organization-matches/organization/{id}/notHidden")
    @Timed
    public List<OrganizationMatchDTO> getNotHiddenOrganizationMatchesForOrganization(@PathVariable UUID id) {
        return organizationMatchService.findAllNotHiddenForOrganization(id);
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

    /**
     * POST  /organization-matches/:id/dismiss : Dismiss organizationMatch.
     *
     * @param id the id of the organizationMatchDTO to dismiss
     * @param dismissMatchDTO match dismiss properties (comment)
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/organization-matches/{id}/dismiss")
    @Timed
    public ResponseEntity<Void> dismissOrganizationMatch(@PathVariable UUID id,
        @RequestBody DismissMatchDTO dismissMatchDTO) {
        log.debug("REST request to dismiss OrganizationMatch : {}", id);
        organizationMatchService.dismissOrganizationMatch(id, dismissMatchDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * POST  /organization-matches/:id/revertDismiss : Revert dismiss organizationMatch.
     *
     * @param id the id of the organizationMatchDTO to revert the dismiss
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/organization-matches/{id}/revertDismiss")
    @Timed
    public ResponseEntity<Void> revertDismissOrganizationMatch(@PathVariable UUID id) {
        log.debug("REST request to revert dismiss of OrganizationMatch : {}", id);
        organizationMatchService.revertDismissOrganizationMatch(id);
        return ResponseEntity.ok().build();
    }

    /**
     * POST  /organization-matches/:id/hide : Hide organizationMatch.
     *
     * @param id the id of the organizationMatchDTO to hide
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/organization-matches/{id}/hide")
    @Timed
    public ResponseEntity<Void> hideOrganizationMatch(@PathVariable UUID id) {
        log.debug("REST request to hide OrganizationMatch : {}", id);
        organizationMatchService.hideOrganizationMatch(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/organization-matches/hideList")
    @Timed
    public ResponseEntity<Void> hideListOfOrganizationMatches(@RequestBody List<UUID> matchIds) {
        log.debug("REST request to hide OrganizationMatch list");
        organizationMatchService.hideOrganizationMatches(matchIds);
        return ResponseEntity.ok().build();
    }

    /**
     * POST  /organization-matches/:id/revertHide : Revert hide organizationMatch.
     *
     * @param id the id of the organizationMatchDTO to revert the hide
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/organization-matches/{id}/revertHide")
    @Timed
    public ResponseEntity<Void> revertHidesOrganizationMatch(@PathVariable UUID id) {
        log.debug("REST request to revert hide of OrganizationMatch : {}", id);
        organizationMatchService.revertHideOrganizationMatch(id);
        return ResponseEntity.ok().build();
    }
}
