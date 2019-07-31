package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.domain.Beds;
import org.benetech.servicenet.repository.BedsRepository;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

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
 * REST controller for managing Beds.
 */
@RestController
@RequestMapping("/api")
public class BedsResource {

    private final Logger log = LoggerFactory.getLogger(BedsResource.class);

    private static final String ENTITY_NAME = "beds";

    private final BedsRepository bedsRepository;

    public BedsResource(BedsRepository bedsRepository) {
        this.bedsRepository = bedsRepository;
    }

    /**
     * POST  /beds : Create a new beds.
     *
     * @param beds the beds to create
     * @return the ResponseEntity with status 201 (Created) and with body the new beds,
     * or with status 400 (Bad Request) if the beds has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/beds")
    public ResponseEntity<Beds> createBeds(@RequestBody Beds beds) throws URISyntaxException {
        log.debug("REST request to save Beds : {}", beds);
        if (beds.getId() != null) {
            throw new BadRequestAlertException("A new beds cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Beds result = bedsRepository.save(beds);
        return ResponseEntity.created(new URI("/api/beds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /beds : Updates an existing beds.
     *
     * @param beds the beds to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated beds,
     * or with status 400 (Bad Request) if the beds is not valid,
     * or with status 500 (Internal Server Error) if the beds couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/beds")
    public ResponseEntity<Beds> updateBeds(@RequestBody Beds beds) throws URISyntaxException {
        log.debug("REST request to update Beds : {}", beds);
        if (beds.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Beds result = bedsRepository.save(beds);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, beds.getId().toString()))
            .body(result);
    }

    /**
     * GET  /beds : get all the beds.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of beds in body
     */
    @GetMapping("/beds")
    public ResponseEntity<List<Beds>> getAllBeds(Pageable pageable) {
        log.debug("REST request to get all Beds");
        Page<Beds> page = bedsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/beds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /beds/:id : get the "id" beds.
     *
     * @param id the id of the beds to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the beds, or with status 404 (Not Found)
     */
    @GetMapping("/beds/{id}")
    public ResponseEntity<Beds> getBeds(@PathVariable UUID id) {
        log.debug("REST request to get Beds : {}", id);
        Optional<Beds> beds = bedsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(beds);
    }

    /**
     * DELETE  /beds/:id : delete the "id" beds.
     *
     * @param id the id of the beds to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/beds/{id}")
    public ResponseEntity<Void> deleteBeds(@PathVariable UUID id) {
        log.debug("REST request to delete Beds : {}", id);
        bedsRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
