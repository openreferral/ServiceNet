package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.ProgramService;
import org.benetech.servicenet.service.dto.ProgramDTO;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Program.
 */
@RestController
@RequestMapping("/api")
public class ProgramResource {

    private static final String ENTITY_NAME = "program";
    private final Logger log = LoggerFactory.getLogger(ProgramResource.class);
    private final ProgramService programService;

    public ProgramResource(ProgramService programService) {
        this.programService = programService;
    }

    /**
     * POST  /programs : Create a new program.
     *
     * @param programDTO the programDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new programDTO,
     * or with status 400 (Bad Request) if the program has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/programs")
    @Timed
    public ResponseEntity<ProgramDTO> createProgram(@Valid @RequestBody ProgramDTO programDTO) throws URISyntaxException {
        log.debug("REST request to save Program : {}", programDTO);
        if (programDTO.getId() != null) {
            throw new BadRequestAlertException("A new program cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProgramDTO result = programService.save(programDTO);
        return ResponseEntity.created(new URI("/api/programs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /programs : Updates an existing program.
     *
     * @param programDTO the programDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated programDTO,
     * or with status 400 (Bad Request) if the programDTO is not valid,
     * or with status 500 (Internal Server Error) if the programDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/programs")
    @Timed
    public ResponseEntity<ProgramDTO> updateProgram(@Valid @RequestBody ProgramDTO programDTO) throws URISyntaxException {
        log.debug("REST request to update Program : {}", programDTO);
        if (programDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProgramDTO result = programService.save(programDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /programs : get all the programs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of programs in body
     */
    @GetMapping("/programs")
    @Timed
    public List<ProgramDTO> getAllPrograms() {
        log.debug("REST request to get all Programs");
        return programService.findAll();
    }

    /**
     * GET  /programs/:id : get the "id" program.
     *
     * @param id the id of the programDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the programDTO, or with status 404 (Not Found)
     */
    @GetMapping("/programs/{id}")
    @Timed
    public ResponseEntity<ProgramDTO> getProgram(@PathVariable UUID id) {
        log.debug("REST request to get Program : {}", id);
        Optional<ProgramDTO> programDTO = programService.findOne(id);
        return ResponseUtil.wrapOrNotFound(programDTO);
    }

    /**
     * DELETE  /programs/:id : delete the "id" program.
     *
     * @param id the id of the programDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/programs/{id}")
    @Timed
    public ResponseEntity<Void> deleteProgram(@PathVariable UUID id) {
        log.debug("REST request to delete Program : {}", id);
        programService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
