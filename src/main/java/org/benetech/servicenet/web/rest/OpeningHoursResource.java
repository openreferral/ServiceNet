package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
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
 * REST controller for managing OpeningHours.
 */
@RestController
@RequestMapping("/api")
public class OpeningHoursResource {

    private final Logger log = LoggerFactory.getLogger(OpeningHoursResource.class);

    private static final String ENTITY_NAME = "openingHours";

    private final OpeningHoursService openingHoursService;

    public OpeningHoursResource(OpeningHoursService openingHoursService) {
        this.openingHoursService = openingHoursService;
    }

    /**
     * POST  /opening-hours : Create a new openingHours.
     *
     * @param openingHoursDTO the openingHoursDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new openingHoursDTO,
     * or with status 400 (Bad Request) if the openingHours has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/opening-hours")
    @Timed
    public ResponseEntity<OpeningHoursDTO> createOpeningHours(@Valid @RequestBody OpeningHoursDTO openingHoursDTO)
        throws URISyntaxException {
        log.debug("REST request to save OpeningHours : {}", openingHoursDTO);
        if (openingHoursDTO.getId() != null) {
            throw new BadRequestAlertException("A new openingHours cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OpeningHoursDTO result = openingHoursService.save(openingHoursDTO);
        return ResponseEntity.created(new URI("/api/opening-hours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /opening-hours : Updates an existing openingHours.
     *
     * @param openingHoursDTO the openingHoursDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated openingHoursDTO,
     * or with status 400 (Bad Request) if the openingHoursDTO is not valid,
     * or with status 500 (Internal Server Error) if the openingHoursDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/opening-hours")
    @Timed
    public ResponseEntity<OpeningHoursDTO> updateOpeningHours(@Valid @RequestBody OpeningHoursDTO openingHoursDTO)
        throws URISyntaxException {
        log.debug("REST request to update OpeningHours : {}", openingHoursDTO);
        if (openingHoursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OpeningHoursDTO result = openingHoursService.save(openingHoursDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, openingHoursDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /opening-hours : get all the openingHours.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of openingHours in body
     */
    @GetMapping("/opening-hours")
    @Timed
    public ResponseEntity<List<OpeningHoursDTO>> getAllOpeningHours(Pageable pageable) {
        log.debug("REST request to get all OpeningHours");
        Page<OpeningHoursDTO> page = openingHoursService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/opening-hours");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /opening-hours/:id : get the "id" openingHours.
     *
     * @param id the id of the openingHoursDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the openingHoursDTO, or with status 404 (Not Found)
     */
    @GetMapping("/opening-hours/{id}")
    @Timed
    public ResponseEntity<OpeningHoursDTO> getOpeningHours(@PathVariable UUID id) {
        log.debug("REST request to get OpeningHours : {}", id);
        Optional<OpeningHoursDTO> openingHoursDTO = openingHoursService.findOne(id);
        return ResponseUtil.wrapOrNotFound(openingHoursDTO);
    }

    /**
     * DELETE  /opening-hours/:id : delete the "id" openingHours.
     *
     * @param id the id of the openingHoursDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/opening-hours/{id}")
    @Timed
    public ResponseEntity<Void> deleteOpeningHours(@PathVariable UUID id) {
        log.debug("REST request to delete OpeningHours : {}", id);
        openingHoursService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
