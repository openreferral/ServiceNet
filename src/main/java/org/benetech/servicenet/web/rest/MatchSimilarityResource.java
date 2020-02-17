package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.service.MatchSimilarityService;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.benetech.servicenet.service.dto.MatchSimilarityDTO;

import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.MatchSimilarity}.
 */
@RestController
@RequestMapping("/api")
public class MatchSimilarityResource {

    private final Logger log = LoggerFactory.getLogger(MatchSimilarityResource.class);

    private static final String ENTITY_NAME = "matchSimilarity";

    private final MatchSimilarityService matchSimilarityService;

    public MatchSimilarityResource(MatchSimilarityService matchSimilarityService) {
        this.matchSimilarityService = matchSimilarityService;
    }

    /**
     * {@code POST  /match-similarities} : Create a new matchSimilarity.
     *
     * @param matchSimilarityDTO the matchSimilarityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matchSimilarityDTO,
     * or with status {@code 400 (Bad Request)} if the matchSimilarity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/match-similarities")
    public ResponseEntity<MatchSimilarityDTO> createMatchSimilarity(@RequestBody MatchSimilarityDTO matchSimilarityDTO)
        throws URISyntaxException {
        log.debug("REST request to save MatchSimilarity : {}", matchSimilarityDTO);
        if (matchSimilarityDTO.getId() != null) {
            throw new BadRequestAlertException("A new matchSimilarity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MatchSimilarityDTO result = matchSimilarityService.saveOrUpdate(matchSimilarityDTO);
        return ResponseEntity.created(new URI("/api/match-similarities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /match-similarities} : Updates an existing matchSimilarity.
     *
     * @param matchSimilarityDTO the matchSimilarityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matchSimilarityDTO,
     * or with status {@code 400 (Bad Request)} if the matchSimilarityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matchSimilarityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/match-similarities")
    public ResponseEntity<MatchSimilarityDTO> updateMatchSimilarity(@RequestBody MatchSimilarityDTO matchSimilarityDTO)
        throws URISyntaxException {
        log.debug("REST request to update MatchSimilarity : {}", matchSimilarityDTO);
        if (matchSimilarityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MatchSimilarityDTO result = matchSimilarityService.saveOrUpdate(matchSimilarityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, matchSimilarityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /match-similarities} : get all the matchSimilarities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matchSimilarities in body.
     */
    @GetMapping("/match-similarities")
    public ResponseEntity<List<MatchSimilarityDTO>> getAllMatchSimilarities(Pageable pageable) {
        log.debug("REST request to get all MatchSimilarities");
        Page<MatchSimilarityDTO> page = matchSimilarityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/match-similarities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /match-similarities/:id} : get the "id" matchSimilarity.
     *
     * @param id the id of the matchSimilarityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matchSimilarityDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/match-similarities/{id}")
    public ResponseEntity<MatchSimilarityDTO> getMatchSimilarity(@PathVariable UUID id) {
        log.debug("REST request to get MatchSimilarity : {}", id);
        Optional<MatchSimilarityDTO> matchSimilarityDTO = matchSimilarityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matchSimilarityDTO);
    }

    /**
     * {@code DELETE  /match-similarities/:id} : delete the "id" matchSimilarity.
     *
     * @param id the id of the matchSimilarityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/match-similarities/{id}")
    public ResponseEntity<Void> deleteMatchSimilarity(@PathVariable UUID id) {
        log.debug("REST request to delete MatchSimilarity : {}", id);
        matchSimilarityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
            .build();
    }
}
