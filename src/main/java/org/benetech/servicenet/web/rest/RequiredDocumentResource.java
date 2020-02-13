package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.dto.RequiredDocumentDTO;
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
 * REST controller for managing RequiredDocument.
 */
@RestController
@RequestMapping("/api")
public class RequiredDocumentResource {

    private static final String ENTITY_NAME = "requiredDocument";
    private final Logger log = LoggerFactory.getLogger(RequiredDocumentResource.class);
    private final RequiredDocumentService requiredDocumentService;

    public RequiredDocumentResource(RequiredDocumentService requiredDocumentService) {
        this.requiredDocumentService = requiredDocumentService;
    }

    /**
     * POST  /required-documents : Create a new requiredDocument.
     *
     * @param requiredDocumentDTO the requiredDocumentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new requiredDocumentDTO,
     * or with status 400 (Bad Request) if the requiredDocument has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/required-documents")
    @Timed
    public ResponseEntity<RequiredDocumentDTO> createRequiredDocument(
        @Valid @RequestBody RequiredDocumentDTO requiredDocumentDTO) throws URISyntaxException {
        log.debug("REST request to save RequiredDocument : {}", requiredDocumentDTO);
        if (requiredDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new requiredDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequiredDocumentDTO result = requiredDocumentService.save(requiredDocumentDTO);
        return ResponseEntity.created(new URI("/api/required-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /required-documents : Updates an existing requiredDocument.
     *
     * @param requiredDocumentDTO the requiredDocumentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated requiredDocumentDTO,
     * or with status 400 (Bad Request) if the requiredDocumentDTO is not valid,
     * or with status 500 (Internal Server Error) if the requiredDocumentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/required-documents")
    @Timed
    public ResponseEntity<RequiredDocumentDTO> updateRequiredDocument(
        @Valid @RequestBody RequiredDocumentDTO requiredDocumentDTO) throws URISyntaxException {
        log.debug("REST request to update RequiredDocument : {}", requiredDocumentDTO);
        if (requiredDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequiredDocumentDTO result = requiredDocumentService.save(requiredDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, requiredDocumentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /required-documents : get all the requiredDocuments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of requiredDocuments in body
     */
    @GetMapping("/required-documents")
    @Timed
    public ResponseEntity<List<RequiredDocumentDTO>> getAllRequiredDocuments(Pageable pageable) {
        log.debug("REST request to get all RequiredDocuments");
        Page<RequiredDocumentDTO> page = requiredDocumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "required-documents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /required-documents/:id : get the "id" requiredDocument.
     *
     * @param id the id of the requiredDocumentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the requiredDocumentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/required-documents/{id}")
    @Timed
    public ResponseEntity<RequiredDocumentDTO> getRequiredDocument(@PathVariable UUID id) {
        log.debug("REST request to get RequiredDocument : {}", id);
        Optional<RequiredDocumentDTO> requiredDocumentDTO = requiredDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requiredDocumentDTO);
    }

    /**
     * DELETE  /required-documents/:id : delete the "id" requiredDocument.
     *
     * @param id the id of the requiredDocumentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/required-documents/{id}")
    @Timed
    public ResponseEntity<Void> deleteRequiredDocument(@PathVariable UUID id) {
        log.debug("REST request to delete RequiredDocument : {}", id);
        requiredDocumentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
