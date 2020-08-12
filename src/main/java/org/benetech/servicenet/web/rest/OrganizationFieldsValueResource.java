package org.benetech.servicenet.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.OrganizationFieldsValueService;
import org.benetech.servicenet.service.dto.OrganizationFieldsValueDTO;
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
 * REST controller for managing {@link org.benetech.servicenet.domain.OrganizationFieldsValue}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationFieldsValueResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationFieldsValueResource.class);

    private static final String ENTITY_NAME = "organizationFieldsValue";

    private final OrganizationFieldsValueService organizationFieldsValueService;

    public OrganizationFieldsValueResource(OrganizationFieldsValueService organizationFieldsValueService) {
        this.organizationFieldsValueService = organizationFieldsValueService;
    }

    /**
     * {@code POST  /organization-fields-values} : Create a new organizationFieldsValue.
     *
     * @param organizationFieldsValueDTO the organizationFieldsValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
     * organizationFieldsValueDTO, or with status {@code 400 (Bad Request)} if the organizationFieldsValue has already
     * an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/organization-fields-values")
    public ResponseEntity<OrganizationFieldsValueDTO> createOrganizationFieldsValue(
        @Valid @RequestBody OrganizationFieldsValueDTO organizationFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to save OrganizationFieldsValue : {}", organizationFieldsValueDTO);
        if (organizationFieldsValueDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new organizationFieldsValue cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        OrganizationFieldsValueDTO result = organizationFieldsValueService.save(organizationFieldsValueDTO);
        return ResponseEntity.created(new URI("/api/organization-fields-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organization-fields-values} : Updates an existing organizationFieldsValue.
     *
     * @param organizationFieldsValueDTO the organizationFieldsValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationFieldsValueDTO,
     * or with status {@code 400 (Bad Request)} if the organizationFieldsValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationFieldsValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/organization-fields-values")
    public ResponseEntity<OrganizationFieldsValueDTO> updateOrganizationFieldsValue(
        @Valid @RequestBody OrganizationFieldsValueDTO organizationFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrganizationFieldsValue : {}", organizationFieldsValueDTO);
        if (organizationFieldsValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationFieldsValueDTO result = organizationFieldsValueService.save(organizationFieldsValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organizationFieldsValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /organization-fields-values} : get all the organizationFieldsValues.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationFieldsValues in body.
     */
    @GetMapping("/organization-fields-values")
    public ResponseEntity<List<OrganizationFieldsValueDTO>> getAllOrganizationFieldsValues(
        Pageable pageable) {
        log.debug("REST request to get all OrganizationFieldsValues");
        Page<OrganizationFieldsValueDTO> page = organizationFieldsValueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organization-fields-values");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /organization-fields-values/:id} : get the "id" organizationFieldsValue.
     *
     * @param id the id of the organizationFieldsValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationFieldsValueDTO, or
     * with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-fields-values/{id}")
    public ResponseEntity<OrganizationFieldsValueDTO> getOrganizationFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to get OrganizationFieldsValue : {}", id);
        Optional<OrganizationFieldsValueDTO> organizationFieldsValueDTO = organizationFieldsValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationFieldsValueDTO);
    }

    /**
     * {@code DELETE  /organization-fields-values/:id} : delete the "id" organizationFieldsValue.
     *
     * @param id the id of the organizationFieldsValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/organization-fields-values/{id}")
    public ResponseEntity<Void> deleteOrganizationFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to delete OrganizationFieldsValue : {}", id);
        organizationFieldsValueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
