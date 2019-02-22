package org.benetech.servicenet.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.service.GeocodingResultService;
import org.benetech.servicenet.service.dto.GeocodingResultDTO;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.springframework.http.ResponseEntity;
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
 * REST controller for managing GeocodingResult.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class GeocodingResultResource {

    private static final String ENTITY_NAME = "geocodingResult";

    private final GeocodingResultService geocodingResultService;

    public GeocodingResultResource(GeocodingResultService geocodingResultService) {
        this.geocodingResultService = geocodingResultService;
    }

    /**
     * POST  /geocoding-results : Create a new geocodingResult.
     *
     * @param geocodingResultDTO the geocodingResultDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new geocodingResultDTO,
     * or with status 400 (Bad Request) if the geocodingResult has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/geocoding-results")
    public ResponseEntity<GeocodingResultDTO> createGeocodingResult(
        @Valid @RequestBody GeocodingResultDTO geocodingResultDTO) throws URISyntaxException {
        log.debug("REST request to save GeocodingResult : {}", geocodingResultDTO);
        if (geocodingResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new geocodingResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeocodingResultDTO result = geocodingResultService.save(geocodingResultDTO);
        return ResponseEntity.created(new URI("/api/geocoding-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /geocoding-results : Updates an existing geocodingResult.
     *
     * @param geocodingResultDTO the geocodingResultDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated geocodingResultDTO,
     * or with status 400 (Bad Request) if the geocodingResultDTO is not valid,
     * or with status 500 (Internal Server Error) if the geocodingResultDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/geocoding-results")
    public ResponseEntity<GeocodingResultDTO> updateGeocodingResult(
        @Valid @RequestBody GeocodingResultDTO geocodingResultDTO) throws URISyntaxException {
        log.debug("REST request to update GeocodingResult : {}", geocodingResultDTO);
        if (geocodingResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GeocodingResultDTO result = geocodingResultService.save(geocodingResultDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, geocodingResultDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /geocoding-results : get all the geocodingResults.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of geocodingResults in body
     */
    @GetMapping("/geocoding-results")
    public List<GeocodingResultDTO> getAllGeocodingResults() {
        log.debug("REST request to get all GeocodingResults");
        return geocodingResultService.findAll();
    }

    /**
     * GET  /geocoding-results/:id : get the "id" geocodingResult.
     *
     * @param id the id of the geocodingResultDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the geocodingResultDTO, or with status 404 (Not Found)
     */
    @GetMapping("/geocoding-results/{id}")
    public ResponseEntity<GeocodingResultDTO> getGeocodingResult(@PathVariable UUID id) {
        log.debug("REST request to get GeocodingResult : {}", id);
        Optional<GeocodingResultDTO> geocodingResultDTO = geocodingResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geocodingResultDTO);
    }

    /**
     * DELETE  /geocoding-results/:id : delete the "id" geocodingResult.
     *
     * @param id the id of the geocodingResultDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/geocoding-results/{id}")
    public ResponseEntity<Void> deleteGeocodingResult(@PathVariable UUID id) {
        log.debug("REST request to delete GeocodingResult : {}", id);
        geocodingResultService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
