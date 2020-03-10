package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.service.RequestLoggerService;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.service.dto.RequestLoggerDTO;

import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.RequestLogger}.
 */
@RestController
@RequestMapping("/api")
public class RequestLoggerResource {

    private final Logger log = LoggerFactory.getLogger(RequestLoggerResource.class);

    private static final String ENTITY_NAME = "serviceNetRequestLogger";

    private final RequestLoggerService requestLoggerService;

    public RequestLoggerResource(RequestLoggerService requestLoggerService) {
        this.requestLoggerService = requestLoggerService;
    }

    /**
     * {@code POST  /request-logger} : Create a new requestLogger.
     *
     * @param requestLoggerDTO the requestLoggerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requestLoggerDTO, or with
     * status {@code 400 (Bad Request)} if the requestLogger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/request-logger")
    public ResponseEntity<RequestLoggerDTO> createRequestLogger(@RequestBody RequestLoggerDTO requestLoggerDTO)
        throws URISyntaxException {
        log.debug("REST request to save RequestLogger : {}", requestLoggerDTO);
        if (requestLoggerDTO.getId() != null) {
            throw new BadRequestAlertException("A new requestLogger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequestLoggerDTO result = requestLoggerService.save(requestLoggerDTO);
        return ResponseEntity.created(new URI("/api/request-logger/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /request-logger} : Updates an existing requestLogger.
     *
     * @param requestLoggerDTO the requestLoggerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestLoggerDTO,
     * or with status {@code 400 (Bad Request)} if the requestLoggerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requestLoggerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/request-logger")
    public ResponseEntity<RequestLoggerDTO> updateRequestLogger(@RequestBody RequestLoggerDTO requestLoggerDTO)
        throws URISyntaxException {
        log.debug("REST request to update RequestLogger : {}", requestLoggerDTO);
        if (requestLoggerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequestLoggerDTO result = requestLoggerService.save(requestLoggerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, requestLoggerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /request-logger} : get all the requestLoggers.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requestLoggers in body.
     */
    @GetMapping("/request-logger")
    public ResponseEntity<List<RequestLoggerDTO>> getAllRequestLoggers(Pageable pageable) {
        log.debug("REST request to get a page of RequestLoggers");
        Page<RequestLoggerDTO> page = requestLoggerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/request-logger");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /request-logger/:id} : get the "id" requestLogger.
     *
     * @param id the id of the requestLoggerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestLoggerDTO, or with status
     * {@code 404 (Not Found)}.
     */
    @GetMapping("/request-logger/{id}")
    public ResponseEntity<RequestLoggerDTO> getRequestLogger(@PathVariable UUID id) {
        log.debug("REST request to get RequestLogger : {}", id);
        Optional<RequestLoggerDTO> requestLoggerDTO = requestLoggerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestLoggerDTO);
    }

    /**
     * {@code DELETE  /request-logger/:id} : delete the "id" requestLogger.
     *
     * @param id the id of the requestLoggerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/request-logger/{id}")
    public ResponseEntity<Void> deleteRequestLogger(@PathVariable UUID id) {
        log.debug("REST request to delete RequestLogger : {}", id);
        requestLoggerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
