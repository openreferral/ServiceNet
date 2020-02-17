package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
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
 * REST controller for managing PhysicalAddress.
 */
@RestController
@RequestMapping("/api")
public class PhysicalAddressResource {

    private static final String ENTITY_NAME = "physicalAddress";
    private final Logger log = LoggerFactory.getLogger(PhysicalAddressResource.class);
    private final PhysicalAddressService physicalAddressService;

    public PhysicalAddressResource(PhysicalAddressService physicalAddressService) {
        this.physicalAddressService = physicalAddressService;
    }

    /**
     * POST  /physical-addresses : Create a new physicalAddress.
     *
     * @param physicalAddressDTO the physicalAddressDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new physicalAddressDTO,
     * or with status 400 (Bad Request) if the physicalAddress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/physical-addresses")
    @Timed
    public ResponseEntity<PhysicalAddressDTO> createPhysicalAddress(
        @Valid @RequestBody PhysicalAddressDTO physicalAddressDTO) throws URISyntaxException {
        log.debug("REST request to save PhysicalAddress : {}", physicalAddressDTO);
        if (physicalAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new physicalAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhysicalAddressDTO result = physicalAddressService.save(physicalAddressDTO);
        return ResponseEntity.created(new URI("/api/physical-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /physical-addresses : Updates an existing physicalAddress.
     *
     * @param physicalAddressDTO the physicalAddressDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated physicalAddressDTO,
     * or with status 400 (Bad Request) if the physicalAddressDTO is not valid,
     * or with status 500 (Internal Server Error) if the physicalAddressDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/physical-addresses")
    @Timed
    public ResponseEntity<PhysicalAddressDTO> updatePhysicalAddress(
        @Valid @RequestBody PhysicalAddressDTO physicalAddressDTO) throws URISyntaxException {
        log.debug("REST request to update PhysicalAddress : {}", physicalAddressDTO);
        if (physicalAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PhysicalAddressDTO result = physicalAddressService.save(physicalAddressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, physicalAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /physical-addresses : get all the physicalAddresses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of physicalAddresses in body
     */
    @GetMapping("/physical-addresses")
    @Timed
    public ResponseEntity<List<PhysicalAddressDTO>> getAllPhysicalAddresses(Pageable pageable) {
        log.debug("REST request to get all PhysicalAddresses");
        Page<PhysicalAddressDTO> page = physicalAddressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/physical-address");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /physical-addresses/:id : get the "id" physicalAddress.
     *
     * @param id the id of the physicalAddressDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the physicalAddressDTO, or with status 404 (Not Found)
     */
    @GetMapping("/physical-addresses/{id}")
    @Timed
    public ResponseEntity<PhysicalAddressDTO> getPhysicalAddress(@PathVariable UUID id) {
        log.debug("REST request to get PhysicalAddress : {}", id);
        Optional<PhysicalAddressDTO> physicalAddressDTO = physicalAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(physicalAddressDTO);
    }

    /**
     * DELETE  /physical-addresses/:id : delete the "id" physicalAddress.
     *
     * @param id the id of the physicalAddressDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/physical-addresses/{id}")
    @Timed
    public ResponseEntity<Void> deletePhysicalAddress(@PathVariable UUID id) {
        log.debug("REST request to delete PhysicalAddress : {}", id);
        physicalAddressService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
