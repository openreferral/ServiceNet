package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.service.DailyUpdateService;
import org.benetech.servicenet.service.dto.DailyUpdateDTO;

import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.DailyUpdate}.
 */
@RestController
@RequestMapping("/api")
public class DailyUpdateResource {

    private final Logger log = LoggerFactory.getLogger(DailyUpdateResource.class);

    private static final String ENTITY_NAME = "dailyUpdate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DailyUpdateService dailyUpdateService;

    public DailyUpdateResource(DailyUpdateService dailyUpdateService) {
        this.dailyUpdateService = dailyUpdateService;
    }

    /**
     * {@code POST  /daily-updates} : Create a new dailyUpdate.
     *
     * @param dailyUpdateDTO the dailyUpdateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dailyUpdateDTO,
     * or with status {@code 400 (Bad Request)} if the dailyUpdate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/daily-updates")
    public ResponseEntity<DailyUpdateDTO> createDailyUpdate(@Valid @RequestBody DailyUpdateDTO dailyUpdateDTO)
        throws URISyntaxException {
        log.debug("REST request to save DailyUpdate : {}", dailyUpdateDTO);
        if (dailyUpdateDTO.getId() != null) {
            throw new BadRequestAlertException("A new dailyUpdate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DailyUpdateDTO result = dailyUpdateService.save(dailyUpdateDTO);
        return ResponseEntity.created(new URI("/api/daily-updates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /daily-updates} : Updates an existing dailyUpdate.
     *
     * @param dailyUpdateDTO the dailyUpdateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dailyUpdateDTO,
     * or with status {@code 400 (Bad Request)} if the dailyUpdateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dailyUpdateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/daily-updates")
    public ResponseEntity<DailyUpdateDTO> updateDailyUpdate(@Valid @RequestBody DailyUpdateDTO dailyUpdateDTO)
        throws URISyntaxException {
        log.debug("REST request to update DailyUpdate : {}", dailyUpdateDTO);
        if (dailyUpdateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DailyUpdateDTO result = dailyUpdateService.save(dailyUpdateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dailyUpdateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /daily-updates} : get all the dailyUpdates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dailyUpdates in body.
     */
    @GetMapping("/daily-updates")
    public ResponseEntity<List<DailyUpdateDTO>> getAllDailyUpdates(Pageable pageable) {
        log.debug("REST request to get a page of DailyUpdates");
        Page<DailyUpdateDTO> page = dailyUpdateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(), page
        );
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /daily-updates/:id} : get the "id" dailyUpdate.
     *
     * @param id the id of the dailyUpdateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dailyUpdateDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/daily-updates/{id}")
    public ResponseEntity<DailyUpdateDTO> getDailyUpdate(@PathVariable UUID id) {
        log.debug("REST request to get DailyUpdate : {}", id);
        Optional<DailyUpdateDTO> dailyUpdateDTO = dailyUpdateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dailyUpdateDTO);
    }

    /**
     * {@code DELETE  /daily-updates/:id} : delete the "id" dailyUpdate.
     *
     * @param id the id of the dailyUpdateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/daily-updates/{id}")
    public ResponseEntity<Void> deleteDailyUpdate(@PathVariable UUID id) {
        log.debug("REST request to delete DailyUpdate : {}", id);
        dailyUpdateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
