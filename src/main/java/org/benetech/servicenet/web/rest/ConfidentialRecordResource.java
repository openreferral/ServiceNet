package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.ConfidentialRecordService;
import org.benetech.servicenet.service.dto.ConfidentialRecordDTO;
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
 * REST controller for managing ConfidentialRecord.
 */
@RestController
@RequestMapping("/api")
public class ConfidentialRecordResource {

    private final Logger log = LoggerFactory.getLogger(ConfidentialRecordResource.class);

    private static final String ENTITY_NAME = "confidentialRecord";

    private final ConfidentialRecordService confidentialRecordService;

    public ConfidentialRecordResource(ConfidentialRecordService confidentialRecordService) {
        this.confidentialRecordService = confidentialRecordService;
    }

    /**
     * POST  /confidential-records : Create a new confidentialRecord.
     *
     * @param confidentialRecordDTO the confidentialRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new confidentialRecordDTO, or with status 400 (Bad Request) if the confidentialRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/confidential-records")
    @Timed
    public ResponseEntity<ConfidentialRecordDTO> createConfidentialRecord(@RequestBody ConfidentialRecordDTO confidentialRecordDTO) throws URISyntaxException {
        log.debug("REST request to save ConfidentialRecord : {}", confidentialRecordDTO);
        if (confidentialRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new confidentialRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfidentialRecordDTO result = confidentialRecordService.save(confidentialRecordDTO);
        return ResponseEntity.created(new URI("/api/confidential-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /confidential-records : Updates an existing confidentialRecord.
     *
     * @param confidentialRecordDTO the confidentialRecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated confidentialRecordDTO,
     * or with status 400 (Bad Request) if the confidentialRecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the confidentialRecordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/confidential-records")
    @Timed
    public ResponseEntity<ConfidentialRecordDTO> updateConfidentialRecord(@RequestBody ConfidentialRecordDTO confidentialRecordDTO) throws URISyntaxException {
        log.debug("REST request to update ConfidentialRecord : {}", confidentialRecordDTO);
        if (confidentialRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfidentialRecordDTO result = confidentialRecordService.save(confidentialRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, confidentialRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /confidential-records : get all the confidentialRecords.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of confidentialRecords in body
     */
    @GetMapping("/confidential-records")
    @Timed
    public List<ConfidentialRecordDTO> getAllConfidentialRecords() {
        log.debug("REST request to get all ConfidentialRecords");
        return confidentialRecordService.findAll();
    }

    /**
     * GET  /confidential-records/:id : get the "id" confidentialRecord.
     *
     * @param id the id of the confidentialRecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the confidentialRecordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/confidential-records/{id}")
    @Timed
    public ResponseEntity<ConfidentialRecordDTO> getConfidentialRecord(@PathVariable UUID id) {
        log.debug("REST request to get ConfidentialRecord : {}", id);
        Optional<ConfidentialRecordDTO> confidentialRecordDTO = confidentialRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(confidentialRecordDTO);
    }

    /**
     * DELETE  /confidential-records/:id : delete the "id" confidentialRecord.
     *
     * @param id the id of the confidentialRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/confidential-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfidentialRecord(@PathVariable UUID id) {
        log.debug("REST request to delete ConfidentialRecord : {}", id);
        confidentialRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
