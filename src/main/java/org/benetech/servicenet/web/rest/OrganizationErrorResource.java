package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.domain.OrganizationError;
import org.benetech.servicenet.repository.OrganizationErrorRepository;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.OrganizationError}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationErrorResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationErrorResource.class);

    private static final String ENTITY_NAME = "organizationError";

    private final OrganizationErrorRepository organizationErrorRepository;

    public OrganizationErrorResource(OrganizationErrorRepository organizationErrorRepository) {
        this.organizationErrorRepository = organizationErrorRepository;
    }

    /**
     * {@code POST  /organization-errors} : Create a new organizationError.
     *
     * @param organizationError the organizationError to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationError,
     * or with status {@code 400 (Bad Request)} if the organizationError has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organization-errors")
    public ResponseEntity<OrganizationError> createOrganizationError(@RequestBody OrganizationError organizationError)
        throws URISyntaxException {
        log.debug("REST request to save OrganizationError : {}", organizationError);
        if (organizationError.getId() != null) {
            throw new BadRequestAlertException("A new organizationError cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationError result = organizationErrorRepository.save(organizationError);
        return ResponseEntity.created(new URI("/api/organization-errors/" + result.getId()))
            .headers(HeaderUtil
                .createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organization-errors} : Updates an existing organizationError.
     *
     * @param organizationError the organizationError to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationError,
     * or with status {@code 400 (Bad Request)} if the organizationError is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationError couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organization-errors")
    public ResponseEntity<OrganizationError> updateOrganizationError(@RequestBody OrganizationError organizationError)
        throws URISyntaxException {
        log.debug("REST request to update OrganizationError : {}", organizationError);
        if (organizationError.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationError result = organizationErrorRepository.save(organizationError);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organizationError.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /organization-errors} : get all the organizationErrors.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationErrors in body.
     */
    @GetMapping("/organization-errors")
    public ResponseEntity<List<OrganizationError>> getAllOrganizationErrors(Pageable pageable) {
        log.debug("REST request to get a page of OrganizationErrors");
        Page<OrganizationError> page = organizationErrorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-errors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /organization-errors/:id} : get the "id" organizationError.
     *
     * @param id the id of the organizationError to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationError,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-errors/{id}")
    public ResponseEntity<OrganizationError> getOrganizationError(@PathVariable UUID id) {
        log.debug("REST request to get OrganizationError : {}", id);
        Optional<OrganizationError> organizationError = organizationErrorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(organizationError);
    }

    /**
     * {@code DELETE  /organization-errors/:id} : delete the "id" organizationError.
     *
     * @param id the id of the organizationError to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organization-errors/{id}")
    public ResponseEntity<Void> deleteOrganizationError(@PathVariable UUID id) {
        log.debug("REST request to delete OrganizationError : {}", id);
        organizationErrorRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
