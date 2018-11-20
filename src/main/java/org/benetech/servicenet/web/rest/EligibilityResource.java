package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.dto.EligibilityDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Eligibility.
 */
@RestController
@RequestMapping("/api")
public class EligibilityResource {

    private static final String ENTITY_NAME = "eligibility";
    private final Logger log = LoggerFactory.getLogger(EligibilityResource.class);
    private final EligibilityService eligibilityService;

    public EligibilityResource(EligibilityService eligibilityService) {
        this.eligibilityService = eligibilityService;
    }

    /**
     * POST  /eligibilities : Create a new eligibility.
     *
     * @param eligibilityDTO the eligibilityDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new eligibilityDTO,
     * or with status 400 (Bad Request) if the eligibility has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/eligibilities")
    @Timed
    public ResponseEntity<EligibilityDTO> createEligibility(
        @Valid @RequestBody EligibilityDTO eligibilityDTO) throws URISyntaxException {
        log.debug("REST request to save Eligibility : {}", eligibilityDTO);
        if (eligibilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new eligibility cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EligibilityDTO result = eligibilityService.save(eligibilityDTO);
        return ResponseEntity.created(new URI("/api/eligibilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /eligibilities : Updates an existing eligibility.
     *
     * @param eligibilityDTO the eligibilityDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated eligibilityDTO,
     * or with status 400 (Bad Request) if the eligibilityDTO is not valid,
     * or with status 500 (Internal Server Error) if the eligibilityDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/eligibilities")
    @Timed
    public ResponseEntity<EligibilityDTO> updateEligibility(
        @Valid @RequestBody EligibilityDTO eligibilityDTO) throws URISyntaxException {
        log.debug("REST request to update Eligibility : {}", eligibilityDTO);
        if (eligibilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EligibilityDTO result = eligibilityService.save(eligibilityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, eligibilityDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /eligibilities : get all the eligibilities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of eligibilities in body
     */
    @GetMapping("/eligibilities")
    @Timed
    public List<EligibilityDTO> getAllEligibilities() {
        log.debug("REST request to get all Eligibilities");
        return eligibilityService.findAll();
    }

    /**
     * GET  /eligibilities/:id : get the "id" eligibility.
     *
     * @param id the id of the eligibilityDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eligibilityDTO, or with status 404 (Not Found)
     */
    @GetMapping("/eligibilities/{id}")
    @Timed
    public ResponseEntity<EligibilityDTO> getEligibility(@PathVariable Long id) {
        log.debug("REST request to get Eligibility : {}", id);
        Optional<EligibilityDTO> eligibilityDTO = eligibilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eligibilityDTO);
    }

    /**
     * DELETE  /eligibilities/:id : delete the "id" eligibility.
     *
     * @param id the id of the eligibilityDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/eligibilities/{id}")
    @Timed
    public ResponseEntity<Void> deleteEligibility(@PathVariable Long id) {
        log.debug("REST request to delete Eligibility : {}", id);
        eligibilityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
