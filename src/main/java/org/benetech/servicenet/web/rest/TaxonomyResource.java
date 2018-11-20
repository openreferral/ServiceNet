package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.dto.TaxonomyDTO;
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

/**
 * REST controller for managing Taxonomy.
 */
@RestController
@RequestMapping("/api")
public class TaxonomyResource {

    private static final String ENTITY_NAME = "taxonomy";
    private final Logger log = LoggerFactory.getLogger(TaxonomyResource.class);
    private final TaxonomyService taxonomyService;

    public TaxonomyResource(TaxonomyService taxonomyService) {
        this.taxonomyService = taxonomyService;
    }

    /**
     * POST  /taxonomies : Create a new taxonomy.
     *
     * @param taxonomyDTO the taxonomyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taxonomyDTO,
     * or with status 400 (Bad Request) if the taxonomy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/taxonomies")
    @Timed
    public ResponseEntity<TaxonomyDTO> createTaxonomy(@RequestBody TaxonomyDTO taxonomyDTO) throws URISyntaxException {
        log.debug("REST request to save Taxonomy : {}", taxonomyDTO);
        if (taxonomyDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxonomy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxonomyDTO result = taxonomyService.save(taxonomyDTO);
        return ResponseEntity.created(new URI("/api/taxonomies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taxonomies : Updates an existing taxonomy.
     *
     * @param taxonomyDTO the taxonomyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taxonomyDTO,
     * or with status 400 (Bad Request) if the taxonomyDTO is not valid,
     * or with status 500 (Internal Server Error) if the taxonomyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/taxonomies")
    @Timed
    public ResponseEntity<TaxonomyDTO> updateTaxonomy(@RequestBody TaxonomyDTO taxonomyDTO) throws URISyntaxException {
        log.debug("REST request to update Taxonomy : {}", taxonomyDTO);
        if (taxonomyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TaxonomyDTO result = taxonomyService.save(taxonomyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taxonomyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taxonomies : get all the taxonomies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of taxonomies in body
     */
    @GetMapping("/taxonomies")
    @Timed
    public List<TaxonomyDTO> getAllTaxonomies() {
        log.debug("REST request to get all Taxonomies");
        return taxonomyService.findAll();
    }

    /**
     * GET  /taxonomies/:id : get the "id" taxonomy.
     *
     * @param id the id of the taxonomyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taxonomyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/taxonomies/{id}")
    @Timed
    public ResponseEntity<TaxonomyDTO> getTaxonomy(@PathVariable Long id) {
        log.debug("REST request to get Taxonomy : {}", id);
        Optional<TaxonomyDTO> taxonomyDTO = taxonomyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxonomyDTO);
    }

    /**
     * DELETE  /taxonomies/:id : delete the "id" taxonomy.
     *
     * @param id the id of the taxonomyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/taxonomies/{id}")
    @Timed
    public ResponseEntity<Void> deleteTaxonomy(@PathVariable Long id) {
        log.debug("REST request to delete Taxonomy : {}", id);
        taxonomyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
