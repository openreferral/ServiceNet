package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.HolidayScheduleService;
import org.benetech.servicenet.service.dto.HolidayScheduleDTO;
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
 * REST controller for managing HolidaySchedule.
 */
@RestController
@RequestMapping("/api")
public class HolidayScheduleResource {

    private static final String ENTITY_NAME = "holidaySchedule";
    private final Logger log = LoggerFactory.getLogger(HolidayScheduleResource.class);
    private final HolidayScheduleService holidayScheduleService;

    public HolidayScheduleResource(HolidayScheduleService holidayScheduleService) {
        this.holidayScheduleService = holidayScheduleService;
    }

    /**
     * POST  /holiday-schedules : Create a new holidaySchedule.
     *
     * @param holidayScheduleDTO the holidayScheduleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new holidayScheduleDTO, or with status 400
     * (Bad Request) if the holidaySchedule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/holiday-schedules")
    @Timed
    public ResponseEntity<HolidayScheduleDTO> createHolidaySchedule(
        @Valid @RequestBody HolidayScheduleDTO holidayScheduleDTO) throws URISyntaxException {
        log.debug("REST request to save HolidaySchedule : {}", holidayScheduleDTO);
        if (holidayScheduleDTO.getId() != null) {
            throw new BadRequestAlertException("A new holidaySchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HolidayScheduleDTO result = holidayScheduleService.save(holidayScheduleDTO);
        return ResponseEntity.created(new URI("/api/holiday-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /holiday-schedules : Updates an existing holidaySchedule.
     *
     * @param holidayScheduleDTO the holidayScheduleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated holidayScheduleDTO,
     * or with status 400 (Bad Request) if the holidayScheduleDTO is not valid,
     * or with status 500 (Internal Server Error) if the holidayScheduleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/holiday-schedules")
    @Timed
    public ResponseEntity<HolidayScheduleDTO> updateHolidaySchedule(
        @Valid @RequestBody HolidayScheduleDTO holidayScheduleDTO) throws URISyntaxException {
        log.debug("REST request to update HolidaySchedule : {}", holidayScheduleDTO);
        if (holidayScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HolidayScheduleDTO result = holidayScheduleService.save(holidayScheduleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, holidayScheduleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /holiday-schedules : get all the holidaySchedules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of holidaySchedules in body
     */
    @GetMapping("/holiday-schedules")
    @Timed
    public ResponseEntity<List<HolidayScheduleDTO>> getAllHolidaySchedules(Pageable pageable) {
        log.debug("REST request to get all HolidaySchedules");
        Page<HolidayScheduleDTO> page = holidayScheduleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/holiday-schedules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /holiday-schedules/:id : get the "id" holidaySchedule.
     *
     * @param id the id of the holidayScheduleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the holidayScheduleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/holiday-schedules/{id}")
    @Timed
    public ResponseEntity<HolidayScheduleDTO> getHolidaySchedule(@PathVariable UUID id) {
        log.debug("REST request to get HolidaySchedule : {}", id);
        Optional<HolidayScheduleDTO> holidayScheduleDTO = holidayScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(holidayScheduleDTO);
    }

    /**
     * DELETE  /holiday-schedules/:id : delete the "id" holidaySchedule.
     *
     * @param id the id of the holidayScheduleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/holiday-schedules/{id}")
    @Timed
    public ResponseEntity<Void> deleteHolidaySchedule(@PathVariable UUID id) {
        log.debug("REST request to delete HolidaySchedule : {}", id);
        holidayScheduleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
