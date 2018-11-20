package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.FundingService;
import org.benetech.servicenet.service.dto.FundingDTO;
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
import java.util.UUID;

/**
 * REST controller for managing Funding.
 */
@RestController
@RequestMapping("/api")
public class FundingResource {

    private static final String ENTITY_NAME = "funding";
    private final Logger log = LoggerFactory.getLogger(FundingResource.class);
    private final FundingService fundingService;

    public FundingResource(FundingService fundingService) {
        this.fundingService = fundingService;
    }

    /**
     * POST  /fundings : Create a new funding.
     *
     * @param fundingDTO the fundingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fundingDTO,
     * or with status 400 (Bad Request) if the funding has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fundings")
    @Timed
    public ResponseEntity<FundingDTO> createFunding(@Valid @RequestBody FundingDTO fundingDTO) throws URISyntaxException {
        log.debug("REST request to save Funding : {}", fundingDTO);
        if (fundingDTO.getId() != null) {
            throw new BadRequestAlertException("A new funding cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FundingDTO result = fundingService.save(fundingDTO);
        return ResponseEntity.created(new URI("/api/fundings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fundings : Updates an existing funding.
     *
     * @param fundingDTO the fundingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fundingDTO,
     * or with status 400 (Bad Request) if the fundingDTO is not valid,
     * or with status 500 (Internal Server Error) if the fundingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fundings")
    @Timed
    public ResponseEntity<FundingDTO> updateFunding(@Valid @RequestBody FundingDTO fundingDTO) throws URISyntaxException {
        log.debug("REST request to update Funding : {}", fundingDTO);
        if (fundingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FundingDTO result = fundingService.save(fundingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fundingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fundings : get all the fundings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fundings in body
     */
    @GetMapping("/fundings")
    @Timed
    public List<FundingDTO> getAllFundings() {
        log.debug("REST request to get all Fundings");
        return fundingService.findAll();
    }

    /**
     * GET  /fundings/:id : get the "id" funding.
     *
     * @param id the id of the fundingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fundingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/fundings/{id}")
    @Timed
    public ResponseEntity<FundingDTO> getFunding(@PathVariable UUID id) {
        log.debug("REST request to get Funding : {}", id);
        Optional<FundingDTO> fundingDTO = fundingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fundingDTO);
    }

    /**
     * DELETE  /fundings/:id : delete the "id" funding.
     *
     * @param id the id of the fundingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fundings/{id}")
    @Timed
    public ResponseEntity<Void> deleteFunding(@PathVariable UUID id) {
        log.debug("REST request to delete Funding : {}", id);
        fundingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
