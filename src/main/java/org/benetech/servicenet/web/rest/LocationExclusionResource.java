package org.benetech.servicenet.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.LocationExclusionService;
import org.benetech.servicenet.service.dto.LocationExclusionDTO;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.LocationExclusion}.
 */
@RestController
@RequestMapping("/api")
public class LocationExclusionResource {

    private final Logger log = LoggerFactory.getLogger(LocationExclusionResource.class);

    private static final String ENTITY_NAME = "locationExclusion";

    private final LocationExclusionService locationExclusionService;

    public LocationExclusionResource(LocationExclusionService locationExclusionService) {
        this.locationExclusionService = locationExclusionService;
    }

    /**
     * {@code POST  /location-exclusions} : Create a new locationExclusion.
     *
     * @param locationExclusionDTO the locationExclusionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationExclusionDTO,
     * or with status {@code 400 (Bad Request)} if the locationExclusion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/location-exclusions")
    public ResponseEntity<LocationExclusionDTO> createLocationExclusion(
        @RequestBody LocationExclusionDTO locationExclusionDTO) throws URISyntaxException {
        log.debug("REST request to save LocationExclusion : {}", locationExclusionDTO);
        if (locationExclusionDTO.getId() != null) {
            throw new BadRequestAlertException("A new locationExclusion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationExclusionDTO result = locationExclusionService.save(locationExclusionDTO);
        return ResponseEntity.created(new URI("/api/location-exclusions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-exclusions} : Updates an existing locationExclusion.
     *
     * @param locationExclusionDTO the locationExclusionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationExclusionDTO,
     * or with status {@code 400 (Bad Request)} if the locationExclusionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationExclusionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/location-exclusions")
    public ResponseEntity<LocationExclusionDTO> updateLocationExclusion(
        @RequestBody LocationExclusionDTO locationExclusionDTO) throws URISyntaxException {
        log.debug("REST request to update LocationExclusion : {}", locationExclusionDTO);
        if (locationExclusionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LocationExclusionDTO result = locationExclusionService.save(locationExclusionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, locationExclusionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /location-exclusions} : get all the locationExclusions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationExclusions in body.
     */
    @GetMapping("/location-exclusions")
    public List<LocationExclusionDTO> getAllLocationExclusions() {
        log.debug("REST request to get all LocationExclusions");
        return locationExclusionService.findAll();
    }

    /**
     * {@code GET  /location-exclusions/:id} : get the "id" locationExclusion.
     *
     * @param id the id of the locationExclusionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationExclusionDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-exclusions/{id}")
    public ResponseEntity<LocationExclusionDTO> getLocationExclusion(@PathVariable UUID id) {
        log.debug("REST request to get LocationExclusion : {}", id);
        Optional<LocationExclusionDTO> locationExclusionDTO = locationExclusionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationExclusionDTO);
    }

    /**
     * {@code DELETE  /location-exclusions/:id} : delete the "id" locationExclusion.
     *
     * @param id the id of the locationExclusionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/location-exclusions/{id}")
    public ResponseEntity<Void> deleteLocationExclusion(@PathVariable UUID id) {
        log.debug("REST request to delete LocationExclusion : {}", id);
        locationExclusionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
