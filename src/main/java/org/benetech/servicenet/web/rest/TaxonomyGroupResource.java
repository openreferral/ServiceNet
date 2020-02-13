package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.service.TaxonomyGroupService;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.service.dto.TaxonomyGroupDTO;

import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.TaxonomyGroup}.
 */
@RestController
@RequestMapping("/api")
public class TaxonomyGroupResource {

    private final Logger log = LoggerFactory.getLogger(TaxonomyGroupResource.class);

    private static final String ENTITY_NAME = "taxonomyGroup";

    private final TaxonomyGroupService taxonomyGroupService;

    public TaxonomyGroupResource(TaxonomyGroupService taxonomyGroupService) {
        this.taxonomyGroupService = taxonomyGroupService;
    }

    /**
     * {@code POST  /taxonomy-groups} : Create a new taxonomyGroup.
     *
     * @param taxonomyGroupDTO the taxonomyGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxonomyGroupDTO,
     * or with status {@code 400 (Bad Request)} if the taxonomyGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/taxonomy-groups")
    public ResponseEntity<TaxonomyGroupDTO> createTaxonomyGroup(@RequestBody TaxonomyGroupDTO taxonomyGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save TaxonomyGroup : {}", taxonomyGroupDTO);
        if (taxonomyGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxonomyGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxonomyGroupDTO result = taxonomyGroupService.save(taxonomyGroupDTO);
        return ResponseEntity.created(new URI("/api/taxonomy-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /taxonomy-groups} : Updates an existing taxonomyGroup.
     *
     * @param taxonomyGroupDTO the taxonomyGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxonomyGroupDTO,
     * or with status {@code 400 (Bad Request)} if the taxonomyGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxonomyGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/taxonomy-groups")
    public ResponseEntity<TaxonomyGroupDTO> updateTaxonomyGroup(@RequestBody TaxonomyGroupDTO taxonomyGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to update TaxonomyGroup : {}", taxonomyGroupDTO);
        if (taxonomyGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TaxonomyGroupDTO result = taxonomyGroupService.save(taxonomyGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taxonomyGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /taxonomy-groups} : get all the taxonomyGroups.
     *

     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxonomyGroups in body.
     */
    @GetMapping("/taxonomy-groups")
    public ResponseEntity<List<TaxonomyGroupDTO>> getAllTaxonomyGroups(Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of TaxonomyGroups");
        Page<TaxonomyGroupDTO> page;
        if (eagerload) {
            page = taxonomyGroupService.findAllWithEagerRelationships(pageable);
        } else {
            page = taxonomyGroupService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/taxonomy-groups");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /taxonomy-groups/:id} : get the "id" taxonomyGroup.
     *
     * @param id the id of the taxonomyGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxonomyGroupDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/taxonomy-groups/{id}")
    public ResponseEntity<TaxonomyGroupDTO> getTaxonomyGroup(@PathVariable UUID id) {
        log.debug("REST request to get TaxonomyGroup : {}", id);
        Optional<TaxonomyGroupDTO> taxonomyGroupDTO = taxonomyGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxonomyGroupDTO);
    }

    /**
     * {@code DELETE  /taxonomy-groups/:id} : delete the "id" taxonomyGroup.
     *
     * @param id the id of the taxonomyGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/taxonomy-groups/{id}")
    public ResponseEntity<Void> deleteTaxonomyGroup(@PathVariable UUID id) {
        log.debug("REST request to delete TaxonomyGroup : {}", id);
        taxonomyGroupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
