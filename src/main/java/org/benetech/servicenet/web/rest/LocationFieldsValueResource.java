package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.service.LocationFieldsValueService;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.service.dto.LocationFieldsValueDTO;

import org.benetech.servicenet.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.LocationFieldsValue}.
 */
@RestController
@RequestMapping("/api")
public class LocationFieldsValueResource {

    private final Logger log = LoggerFactory.getLogger(LocationFieldsValueResource.class);

    private static final String ENTITY_NAME = "locationFieldsValue";

    private final LocationFieldsValueService locationFieldsValueService;

    public LocationFieldsValueResource(LocationFieldsValueService locationFieldsValueService) {
        this.locationFieldsValueService = locationFieldsValueService;
    }

    /**
     * {@code POST  /location-fields-values} : Create a new locationFieldsValue.
     *
     * @param locationFieldsValueDTO the locationFieldsValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationFieldsValueDTO,
     * or with status {@code 400 (Bad Request)} if the locationFieldsValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/location-fields-values")
    public ResponseEntity<LocationFieldsValueDTO> createLocationFieldsValue(
        @Valid @RequestBody LocationFieldsValueDTO locationFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LocationFieldsValue : {}", locationFieldsValueDTO);
        if (locationFieldsValueDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new locationFieldsValue cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        LocationFieldsValueDTO result = locationFieldsValueService.save(locationFieldsValueDTO);
        return ResponseEntity.created(new URI("/api/location-fields-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-fields-values} : Updates an existing locationFieldsValue.
     *
     * @param locationFieldsValueDTO the locationFieldsValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationFieldsValueDTO,
     * or with status {@code 400 (Bad Request)} if the locationFieldsValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationFieldsValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/location-fields-values")
    public ResponseEntity<LocationFieldsValueDTO> updateLocationFieldsValue(
        @Valid @RequestBody LocationFieldsValueDTO locationFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LocationFieldsValue : {}", locationFieldsValueDTO);
        if (locationFieldsValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LocationFieldsValueDTO result = locationFieldsValueService.save(locationFieldsValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, locationFieldsValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /location-fields-values} : get all the locationFieldsValues.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationFieldsValues in body.
     */
    @GetMapping("/location-fields-values")
    public List<LocationFieldsValueDTO> getAllLocationFieldsValues() {
        log.debug("REST request to get all LocationFieldsValues");
        return locationFieldsValueService.findAll();
    }

    /**
     * {@code GET  /location-fields-values/:id} : get the "id" locationFieldsValue.
     *
     * @param id the id of the locationFieldsValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationFieldsValueDTO, or
     * with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-fields-values/{id}")
    public ResponseEntity<LocationFieldsValueDTO> getLocationFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to get LocationFieldsValue : {}", id);
        Optional<LocationFieldsValueDTO> locationFieldsValueDTO = locationFieldsValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationFieldsValueDTO);
    }

    /**
     * {@code DELETE  /location-fields-values/:id} : delete the "id" locationFieldsValue.
     *
     * @param id the id of the locationFieldsValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/location-fields-values/{id}")
    public ResponseEntity<Void> deleteLocationFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to delete LocationFieldsValue : {}", id);
        locationFieldsValueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
