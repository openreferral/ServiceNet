package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.dto.RegularScheduleDTO;
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
 * REST controller for managing RegularSchedule.
 */
@RestController
@RequestMapping("/api")
public class RegularScheduleResource {

    private static final String ENTITY_NAME = "regularSchedule";
    private final Logger log = LoggerFactory.getLogger(RegularScheduleResource.class);
    private final RegularScheduleService regularScheduleService;

    public RegularScheduleResource(RegularScheduleService regularScheduleService) {
        this.regularScheduleService = regularScheduleService;
    }

    /**
     * POST  /regular-schedules : Create a new regularSchedule.
     *
     * @param regularScheduleDTO the regularScheduleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new regularScheduleDTO,
     * or with status 400 (Bad Request) if the regularSchedule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/regular-schedules")
    @Timed
    public ResponseEntity<RegularScheduleDTO> createRegularSchedule(
        @Valid @RequestBody RegularScheduleDTO regularScheduleDTO) throws URISyntaxException {
        log.debug("REST request to save RegularSchedule : {}", regularScheduleDTO);
        if (regularScheduleDTO.getId() != null) {
            throw new BadRequestAlertException("A new regularSchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegularScheduleDTO result = regularScheduleService.save(regularScheduleDTO);
        return ResponseEntity.created(new URI("/api/regular-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /regular-schedules : Updates an existing regularSchedule.
     *
     * @param regularScheduleDTO the regularScheduleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated regularScheduleDTO,
     * or with status 400 (Bad Request) if the regularScheduleDTO is not valid,
     * or with status 500 (Internal Server Error) if the regularScheduleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/regular-schedules")
    @Timed
    public ResponseEntity<RegularScheduleDTO> updateRegularSchedule(
        @Valid @RequestBody RegularScheduleDTO regularScheduleDTO) throws URISyntaxException {
        log.debug("REST request to update RegularSchedule : {}", regularScheduleDTO);
        if (regularScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegularScheduleDTO result = regularScheduleService.save(regularScheduleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, regularScheduleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /regular-schedules : get all the regularSchedules.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of regularSchedules in body
     */
    @GetMapping("/regular-schedules")
    @Timed
    public ResponseEntity<List<RegularScheduleDTO>> getAllRegularSchedules(Pageable pageable) {
        log.debug("REST request to get all RegularSchedules");
        Page<RegularScheduleDTO> page = regularScheduleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/regular-schedules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /regular-schedules/:id : get the "id" regularSchedule.
     *
     * @param id the id of the regularScheduleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the regularScheduleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/regular-schedules/{id}")
    @Timed
    public ResponseEntity<RegularScheduleDTO> getRegularSchedule(@PathVariable UUID id) {
        log.debug("REST request to get RegularSchedule : {}", id);
        Optional<RegularScheduleDTO> regularScheduleDTO = regularScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(regularScheduleDTO);
    }

    /**
     * DELETE  /regular-schedules/:id : delete the "id" regularSchedule.
     *
     * @param id the id of the regularScheduleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/regular-schedules/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegularSchedule(@PathVariable UUID id) {
        log.debug("REST request to delete RegularSchedule : {}", id);
        regularScheduleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
