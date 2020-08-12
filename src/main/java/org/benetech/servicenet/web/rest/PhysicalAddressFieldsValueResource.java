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
import org.benetech.servicenet.service.PhysicalAddressFieldsValueService;
import org.benetech.servicenet.service.dto.PhysicalAddressFieldsValueDTO;
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
 * REST controller for managing {@link org.benetech.servicenet.domain.PhysicalAddressFieldsValue}.
 */
@RestController
@RequestMapping("/api")
public class PhysicalAddressFieldsValueResource {

    private final Logger log = LoggerFactory.getLogger(PhysicalAddressFieldsValueResource.class);

    private static final String ENTITY_NAME = "physicalAddressFieldsValue";

    private final PhysicalAddressFieldsValueService physicalAddressFieldsValueService;

    public PhysicalAddressFieldsValueResource(PhysicalAddressFieldsValueService physicalAddressFieldsValueService) {
        this.physicalAddressFieldsValueService = physicalAddressFieldsValueService;
    }

    /**
     * {@code POST  /physical-address-fields-values} : Create a new physicalAddressFieldsValue.
     *
     * @param physicalAddressFieldsValueDTO the physicalAddressFieldsValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
     * physicalAddressFieldsValueDTO, or with status {@code 400 (Bad Request)} if the physicalAddressFieldsValue
     * has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/physical-address-fields-values")
    public ResponseEntity<PhysicalAddressFieldsValueDTO> createPhysicalAddressFieldsValue(
        @Valid @RequestBody PhysicalAddressFieldsValueDTO physicalAddressFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PhysicalAddressFieldsValue : {}", physicalAddressFieldsValueDTO);
        if (physicalAddressFieldsValueDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new physicalAddressFieldsValue cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        PhysicalAddressFieldsValueDTO result = physicalAddressFieldsValueService.save(physicalAddressFieldsValueDTO);
        return ResponseEntity.created(new URI("/api/physical-address-fields-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /physical-address-fields-values} : Updates an existing physicalAddressFieldsValue.
     *
     * @param physicalAddressFieldsValueDTO the physicalAddressFieldsValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
     * physicalAddressFieldsValueDTO,
     * or with status {@code 400 (Bad Request)} if the physicalAddressFieldsValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the physicalAddressFieldsValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/physical-address-fields-values")
    public ResponseEntity<PhysicalAddressFieldsValueDTO> updatePhysicalAddressFieldsValue(
        @Valid @RequestBody PhysicalAddressFieldsValueDTO physicalAddressFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PhysicalAddressFieldsValue : {}", physicalAddressFieldsValueDTO);
        if (physicalAddressFieldsValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PhysicalAddressFieldsValueDTO result = physicalAddressFieldsValueService.save(physicalAddressFieldsValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, physicalAddressFieldsValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /physical-address-fields-values} : get all the physicalAddressFieldsValues.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of physicalAddressFieldsValues in body.
     */
    @GetMapping("/physical-address-fields-values")
    public ResponseEntity<List<PhysicalAddressFieldsValueDTO>> getAllPhysicalAddressFieldsValues(
        Pageable pageable) {
        log.debug("REST request to get all PhysicalAddressFieldsValues");
        Page<PhysicalAddressFieldsValueDTO> page = physicalAddressFieldsValueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/programs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /physical-address-fields-values/:id} : get the "id" physicalAddressFieldsValue.
     *
     * @param id the id of the physicalAddressFieldsValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the physicalAddressFieldsValueDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/physical-address-fields-values/{id}")
    public ResponseEntity<PhysicalAddressFieldsValueDTO> getPhysicalAddressFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to get PhysicalAddressFieldsValue : {}", id);
        Optional<PhysicalAddressFieldsValueDTO> physicalAddressFieldsValueDTO = physicalAddressFieldsValueService
            .findOne(id);
        return ResponseUtil.wrapOrNotFound(physicalAddressFieldsValueDTO);
    }

    /**
     * {@code DELETE  /physical-address-fields-values/:id} : delete the "id" physicalAddressFieldsValue.
     *
     * @param id the id of the physicalAddressFieldsValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/physical-address-fields-values/{id}")
    public ResponseEntity<Void> deletePhysicalAddressFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to delete PhysicalAddressFieldsValue : {}", id);
        physicalAddressFieldsValueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
            .build();
    }
}
