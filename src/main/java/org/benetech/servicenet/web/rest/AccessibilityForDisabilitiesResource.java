package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;
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
 * REST controller for managing AccessibilityForDisabilities.
 */
@RestController
@RequestMapping("/api")
public class AccessibilityForDisabilitiesResource {

    private static final String ENTITY_NAME = "accessibilityForDisabilities";
    private final Logger log = LoggerFactory.getLogger(AccessibilityForDisabilitiesResource.class);
    private final AccessibilityForDisabilitiesService accessibilityForDisabilitiesService;

    public AccessibilityForDisabilitiesResource(AccessibilityForDisabilitiesService accessibilityForDisabilitiesService) {
        this.accessibilityForDisabilitiesService = accessibilityForDisabilitiesService;
    }

    /**
     * POST  /accessibility-for-disabilities : Create a new accessibilityForDisabilities.
     *
     * @param accessibilityForDisabilitiesDTO the accessibilityForDisabilitiesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accessibilityForDisabilitiesDTO,
     * or with status 400 (Bad Request) if the accessibilityForDisabilities has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/accessibility-for-disabilities")
    @Timed
    public ResponseEntity<AccessibilityForDisabilitiesDTO> createAccessibilityForDisabilities(
        @Valid @RequestBody AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO) throws URISyntaxException {
        log.debug("REST request to save AccessibilityForDisabilities : {}", accessibilityForDisabilitiesDTO);
        if (accessibilityForDisabilitiesDTO.getId() != null) {
            throw new BadRequestAlertException("A new accessibilityForDisabilities cannot already have an ID", ENTITY_NAME,
                "idexists");
        }
        AccessibilityForDisabilitiesDTO result = accessibilityForDisabilitiesService.save(accessibilityForDisabilitiesDTO);
        return ResponseEntity.created(new URI("/api/accessibility-for-disabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accessibility-for-disabilities : Updates an existing accessibilityForDisabilities.
     *
     * @param accessibilityForDisabilitiesDTO the accessibilityForDisabilitiesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accessibilityForDisabilitiesDTO,
     * or with status 400 (Bad Request) if the accessibilityForDisabilitiesDTO is not valid,
     * or with status 500 (Internal Server Error) if the accessibilityForDisabilitiesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/accessibility-for-disabilities")
    @Timed
    public ResponseEntity<AccessibilityForDisabilitiesDTO> updateAccessibilityForDisabilities(
        @Valid @RequestBody AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO) throws URISyntaxException {
        log.debug("REST request to update AccessibilityForDisabilities : {}", accessibilityForDisabilitiesDTO);
        if (accessibilityForDisabilitiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AccessibilityForDisabilitiesDTO result = accessibilityForDisabilitiesService.save(accessibilityForDisabilitiesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accessibilityForDisabilitiesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accessibility-for-disabilities : get all the accessibilityForDisabilities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accessibilityForDisabilities in body
     */
    @GetMapping("/accessibility-for-disabilities")
    @Timed
    public ResponseEntity<List<AccessibilityForDisabilitiesDTO>> getAllAccessibilityForDisabilities(Pageable pageable) {
        log.debug("REST request to get all AccessibilityForDisabilities");
        Page<AccessibilityForDisabilitiesDTO> page = accessibilityForDisabilitiesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/accessibility-for-disabilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /accessibility-for-disabilities/:id : get the "id" accessibilityForDisabilities.
     *
     * @param id the id of the accessibilityForDisabilitiesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accessibilityForDisabilitiesDTO,
     * or with status 404 (Not Found)
     */
    @GetMapping("/accessibility-for-disabilities/{id}")
    @Timed
    public ResponseEntity<AccessibilityForDisabilitiesDTO> getAccessibilityForDisabilities(@PathVariable UUID id) {
        log.debug("REST request to get AccessibilityForDisabilities : {}", id);
        Optional<AccessibilityForDisabilitiesDTO> accessibilityForDisabilitiesDTO =
            accessibilityForDisabilitiesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accessibilityForDisabilitiesDTO);
    }

    /**
     * DELETE  /accessibility-for-disabilities/:id : delete the "id" accessibilityForDisabilities.
     *
     * @param id the id of the accessibilityForDisabilitiesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/accessibility-for-disabilities/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccessibilityForDisabilities(@PathVariable UUID id) {
        log.debug("REST request to delete AccessibilityForDisabilities : {}", id);
        accessibilityForDisabilitiesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
