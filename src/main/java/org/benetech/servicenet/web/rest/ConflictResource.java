package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.errors.BadRequestAlertException;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Conflict.
 */
@RestController
@RequestMapping("/api")
public class ConflictResource {

    private final Logger log = LoggerFactory.getLogger(ConflictResource.class);

    private static final String ENTITY_NAME = "conflict";

    private final ConflictService conflictService;

    public ConflictResource(ConflictService conflictService) {
        this.conflictService = conflictService;
    }

    /**
     * POST  /conflicts : Create a new conflict.
     *
     * @param conflictDTO the conflictDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new conflictDTO, or with status
     * 400 (Bad Request) if the conflict has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/conflicts")
    @Timed
    public ResponseEntity<ConflictDTO> createConflict(@Valid @RequestBody ConflictDTO conflictDTO)
    throws URISyntaxException {
        log.debug("REST request to save Conflict : {}", conflictDTO);
        if (conflictDTO.getId() != null) {
            throw new BadRequestAlertException("A new conflict cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConflictDTO result = conflictService.save(conflictDTO);
        return ResponseEntity.created(new URI("/api/conflicts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /conflicts : Updates an existing conflict.
     *
     * @param conflictDTO the conflictDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated conflictDTO,
     * or with status 400 (Bad Request) if the conflictDTO is not valid,
     * or with status 500 (Internal Server Error) if the conflictDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/conflicts")
    @Timed
    public ResponseEntity<ConflictDTO> updateConflict(@Valid @RequestBody ConflictDTO conflictDTO)
    throws URISyntaxException {
        log.debug("REST request to update Conflict : {}", conflictDTO);
        if (conflictDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConflictDTO result = conflictService.save(conflictDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, conflictDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /conflicts : get all the conflicts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of conflicts in body
     */
    @GetMapping("/conflicts")
    @Timed
    public ResponseEntity<List<ConflictDTO>> findAll(Pageable pageable) {
        log.debug("REST request to get a page of Conflicts");
        Page<ConflictDTO> page = conflictService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/conflicts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /conflicts/:id : get the "id" conflict.
     *
     * @param id the id of the conflictDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the conflictDTO, or with status 404 (Not Found)
     */
    @GetMapping("/conflicts/{id}")
    @Timed
    public ResponseEntity<ConflictDTO> getConflict(@PathVariable UUID id) {
        log.debug("REST request to get Conflict : {}", id);
        Optional<ConflictDTO> conflictDTO = conflictService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conflictDTO);
    }

    /**
     * DELETE  /conflicts/:id : delete the "id" conflict.
     *
     * @param id the id of the conflictDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/conflicts/{id}")
    @Timed
    public ResponseEntity<Void> deleteConflict(@PathVariable UUID id) {
        log.debug("REST request to delete Conflict : {}", id);
        conflictService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
