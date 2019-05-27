package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.SystemAccountService;
import org.benetech.servicenet.service.dto.SystemAccountDTO;
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
 * REST controller for managing SystemAccount.
 */
@RestController
@RequestMapping("/api")
public class SystemAccountResource {

    private static final String ENTITY_NAME = "systemAccount";
    private final Logger log = LoggerFactory.getLogger(SystemAccountResource.class);
    private final SystemAccountService systemAccountService;

    public SystemAccountResource(SystemAccountService systemAccountService) {
        this.systemAccountService = systemAccountService;
    }

    /**
     * POST  /system-accounts : Create a new systemAccount.
     *
     * @param systemAccountDTO the systemAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new systemAccountDTO,
     * or with status 400 (Bad Request) if the systemAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/system-accounts")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<SystemAccountDTO> createSystemAccount(
        @Valid @RequestBody SystemAccountDTO systemAccountDTO) throws URISyntaxException {
        log.debug("REST request to save SystemAccount : {}", systemAccountDTO);
        if (systemAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemAccountDTO result = systemAccountService.save(systemAccountDTO);
        return ResponseEntity.created(new URI("/api/system-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /system-accounts : Updates an existing systemAccount.
     *
     * @param systemAccountDTO the systemAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated systemAccountDTO,
     * or with status 400 (Bad Request) if the systemAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the systemAccountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/system-accounts")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<SystemAccountDTO> updateSystemAccount(
        @Valid @RequestBody SystemAccountDTO systemAccountDTO) throws URISyntaxException {
        log.debug("REST request to update SystemAccount : {}", systemAccountDTO);
        if (systemAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SystemAccountDTO result = systemAccountService.save(systemAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, systemAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /system-accounts : get all the systemAccounts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of systemAccounts in body
     */
    @GetMapping("/system-accounts")
    @Timed
    public List<SystemAccountDTO> getAllSystemAccounts() {
        log.debug("REST request to get all SystemAccounts");
        return systemAccountService.findAll();
    }

    /**
     * GET  /system-accounts/:id : get the "id" systemAccount.
     *
     * @param id the id of the systemAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the systemAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/system-accounts/{id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<SystemAccountDTO> getSystemAccount(@PathVariable UUID id) {
        log.debug("REST request to get SystemAccount : {}", id);
        Optional<SystemAccountDTO> systemAccountDTO = systemAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemAccountDTO);
    }

    /**
     * DELETE  /system-accounts/:id : delete the "id" systemAccount.
     *
     * @param id the id of the systemAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/system-accounts/{id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteSystemAccount(@PathVariable UUID id) {
        log.debug("REST request to delete SystemAccount : {}", id);
        systemAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
