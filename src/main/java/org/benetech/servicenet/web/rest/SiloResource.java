package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.service.SiloService;
import org.benetech.servicenet.service.dto.SiloDTO;

import org.benetech.servicenet.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.Silo}.
 */
@RestController
@RequestMapping("/api")
public class SiloResource {

    private final Logger log = LoggerFactory.getLogger(SiloResource.class);

    private static final String ENTITY_NAME = "silo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiloService siloService;

    public SiloResource(SiloService siloService) {
        this.siloService = siloService;
    }

    /**
     * {@code POST  /silos} : Create a new silo.
     *
     * @param siloDTO the siloDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
     * siloDTO, or with status {@code 400 (Bad Request)} if the silo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/silos")
    public ResponseEntity<SiloDTO> createSilo(@RequestBody SiloDTO siloDTO) throws URISyntaxException {
        log.debug("REST request to save Silo : {}", siloDTO);
        if (siloDTO.getId() != null) {
            throw new BadRequestAlertException("A new silo cannot already have an ID", ENTITY_NAME, "idexists");
        }

        siloService.findOneByName(siloDTO.getName()).ifPresent(s -> {
            throw new BadRequestAlertException("Name is already used", ENTITY_NAME, "nameunique");
        });

        SiloDTO result = siloService.save(siloDTO);
        return ResponseEntity.created(new URI("/api/silos/" + result.getId()))
            .headers(org.benetech.servicenet.web.rest.util.HeaderUtil
                .createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /silos} : Updates an existing silo.
     *
     * @param siloDTO the siloDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siloDTO,
     * or with status {@code 400 (Bad Request)} if the siloDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siloDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/silos")
    public ResponseEntity<SiloDTO> updateSilo(@RequestBody SiloDTO siloDTO) throws URISyntaxException {
        log.debug("REST request to update Silo : {}", siloDTO);
        if (siloDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        siloService.findOneByName(siloDTO.getName()).ifPresent(s -> {
            if (!s.getId().equals(siloDTO.getId())) {
                throw new BadRequestAlertException("Name is already used", ENTITY_NAME, "nameunique");
            }
        });

        SiloDTO result = siloService.save(siloDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, siloDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /silos} : get all the silos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of silos in body.
     */
    @GetMapping("/silos")
    public ResponseEntity<List<SiloDTO>> getAllPagedSilos(Pageable pageable) {
        log.debug("REST request to get a page of Silos");
        Page<SiloDTO> page = siloService.findPagedAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /silos} : get all the silos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of silos in body.
     */
    @GetMapping("/all-silos")
    public ResponseEntity<List<SiloDTO>> getAllSilos() {
        log.debug("REST request to get a page of Silos");
        List<SiloDTO> page = siloService.findAll();
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /silos/:id} : get the "id" silo.
     *
     * @param id the id of the siloDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siloDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/silos/{id}")
    public ResponseEntity<SiloDTO> getSilo(@PathVariable UUID id) {
        log.debug("REST request to get Silo : {}", id);
        Optional<SiloDTO> siloDTO = siloService.findOne(id);
        return ResponseUtil.wrapOrNotFound(siloDTO);
    }

    /**
     * {@code DELETE  /silos/:id} : delete the "id" silo.
     *
     * @param id the id of the siloDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/silos/{id}")
    public ResponseEntity<Void> deleteSilo(@PathVariable UUID id) {
        log.debug("REST request to delete Silo : {}", id);
        siloService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
