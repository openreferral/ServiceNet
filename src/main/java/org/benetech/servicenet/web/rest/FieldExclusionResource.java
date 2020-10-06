package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.FieldExclusionService;
import org.benetech.servicenet.service.dto.FieldExclusionDTO;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

/**
 * REST controller for managing FieldExclusion.
 */
@RestController
@RequestMapping("/api")
public class FieldExclusionResource {

    private final Logger log = LoggerFactory.getLogger(FieldExclusionResource.class);

    private static final String ENTITY_NAME = "fieldExclusion";

    private final FieldExclusionService fieldExclusionService;

    public FieldExclusionResource(FieldExclusionService fieldExclusionService) {
        this.fieldExclusionService = fieldExclusionService;
    }

    /**
     * POST  /field-exclusions : Create a new fieldExclusion.
     *
     * @param fieldExclusionDTO the fieldExclusionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fieldExclusionDTO,
     * or with status 400 (Bad Request) if the fieldExclusion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/field-exclusions")
    @Timed
    public ResponseEntity<FieldExclusionDTO> createFieldExclusion(
        @RequestBody FieldExclusionDTO fieldExclusionDTO) throws URISyntaxException {
        log.debug("REST request to save FieldExclusion : {}", fieldExclusionDTO);
        if (fieldExclusionDTO.getId() != null) {
            throw new BadRequestAlertException("A new fieldExclusion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FieldExclusionDTO result = fieldExclusionService.save(fieldExclusionDTO);
        return ResponseEntity.created(new URI("/api/field-exclusions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /field-exclusions : Updates an existing fieldExclusion.
     *
     * @param fieldExclusionDTO the fieldExclusionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fieldExclusionDTO,
     * or with status 400 (Bad Request) if the fieldExclusionDTO is not valid,
     * or with status 500 (Internal Server Error) if the fieldExclusionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/field-exclusions")
    @Timed
    public ResponseEntity<FieldExclusionDTO> updateFieldExclusion(
        @RequestBody FieldExclusionDTO fieldExclusionDTO) throws URISyntaxException {
        log.debug("REST request to update FieldExclusion : {}", fieldExclusionDTO);
        if (fieldExclusionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FieldExclusionDTO result = fieldExclusionService.save(fieldExclusionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fieldExclusionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /field-exclusions : get all the fieldExclusions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fieldExclusions in body
     */
    @GetMapping("/field-exclusions")
    @Timed
    public ResponseEntity<List<FieldExclusionDTO>> getAllFieldExclusions(Pageable pageable) {
        log.debug("REST request to get all FieldExclusions");
        Page<FieldExclusionDTO> page = fieldExclusionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/field-exclusions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /field-exclusions/:id : get the "id" fieldExclusion.
     *
     * @param id the id of the fieldExclusionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fieldExclusionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/field-exclusions/{id}")
    @Timed
    public ResponseEntity<FieldExclusionDTO> getFieldExclusion(@PathVariable UUID id) {
        log.debug("REST request to get FieldExclusion : {}", id);
        Optional<FieldExclusionDTO> fieldExclusionDTO = fieldExclusionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fieldExclusionDTO);
    }

    /**
     * DELETE  /field-exclusions/:id : delete the "id" fieldExclusion.
     *
     * @param id the id of the fieldExclusionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/field-exclusions/{id}")
    @Timed
    public ResponseEntity<Void> deleteFieldExclusion(@PathVariable UUID id) {
        log.debug("REST request to delete FieldExclusion : {}", id);
        fieldExclusionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
